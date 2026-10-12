package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.AuthManager
import com.example.data.EcosystemRepository
import com.example.model.EcosystemApp
import com.example.model.UserRole
import com.example.ui.components.EcosystemTopBar
import com.example.ui.customer.CustomerHomeScreen
import com.example.ui.owner.BusinessOwnerDashboardScreen
import com.example.ui.superadmin.SuperAdminDashboardScreen
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var repository: EcosystemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        authManager = AuthManager(this)
        repository = EcosystemRepository(this)

        setContent {
            MyApplicationTheme {
                EcosystemAppRoot(
                    activity = this,
                    authManager = authManager,
                    repository = repository
                )
            }
        }
    }
}

@Composable
fun EcosystemAppRoot(
    activity: ComponentActivity,
    authManager: AuthManager,
    repository: EcosystemRepository
) {
    val coroutineScope = rememberCoroutineScope()
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    var showAuthModal by remember { mutableStateOf(false) }
    var authErrorMsg by remember { mutableStateOf<String?>(null) }
    var isAuthLoading by remember { mutableStateOf(false) }

    // Listen to Firebase auth state
    DisposableEffect(Unit) {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        // Attempt silent sign-in
        authManager.attemptAutoSignIn(
            scope = coroutineScope,
            onSuccess = { currentUser = Firebase.auth.currentUser },
            onFailure = { /* Continue with interactive fallback */ }
        )
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    val currentApp by repository.currentApp.collectAsState()
    val activeTenantId by repository.activeTenantId.collectAsState()
    val allTenants by repository.tenants.collectAsState()
    val activeTenant = allTenants.find { it.id == activeTenantId } ?: allTenants.firstOrNull()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ecosystem_scaffold"),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            EcosystemTopBar(
                currentApp = currentApp,
                activeTenant = activeTenant,
                allTenants = allTenants,
                onSelectApp = { repository.selectApp(it) },
                onSelectTenant = { repository.selectTenant(it) },
                onOpenAuth = { showAuthModal = true },
                userEmail = currentUser?.email
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentApp) {
                EcosystemApp.CUSTOMER_APP -> {
                    CustomerHomeScreen(
                        repository = repository,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                EcosystemApp.BUSINESS_OWNER_APP -> {
                    BusinessOwnerDashboardScreen(
                        repository = repository,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                EcosystemApp.DEVELOPER_SUPER_ADMIN_APP -> {
                    SuperAdminDashboardScreen(
                        repository = repository,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // Auth & Google Sign-In Dialog
    if (showAuthModal) {
        AlertDialog(
            onDismissRequest = { showAuthModal = false },
            title = {
                Text(
                    text = if (currentUser != null) "Authenticated Profile" else "Google Account Authentication",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (currentUser != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Signed in as: ${currentUser?.displayName ?: "User"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = currentUser?.email ?: "user@google.com",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Firebase UID: ${currentUser?.uid?.take(12)}...",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = {
                                authManager.signOut(coroutineScope) {
                                    currentUser = null
                                    showAuthModal = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sign Out")
                        }
                    } else {
                        Text(
                            text = "Authenticate securely using Google Sign-In via Jetpack Credential Manager to synchronize bookings, project milestones, and quotes with Cloud Firestore.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (authErrorMsg != null) {
                            Text(
                                text = authErrorMsg ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp
                            )
                        }

                        // Google Sign-In Button (Mandatory Interactive Button)
                        Button(
                            onClick = {
                                isAuthLoading = true
                                authErrorMsg = null
                                authManager.signInWithGoogle(
                                    activity = activity,
                                    scope = coroutineScope,
                                    onSuccess = {
                                        isAuthLoading = false
                                        currentUser = Firebase.auth.currentUser
                                        showAuthModal = false
                                    },
                                    onError = { error ->
                                        isAuthLoading = false
                                        authErrorMsg = error
                                    },
                                    onCancelled = {
                                        isAuthLoading = false
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("google_sign_in_button"),
                            enabled = !isAuthLoading
                        ) {
                            if (isAuthLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sign in with Google")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAuthModal = false }) {
                    Text("Close")
                }
            }
        )
    }
}
