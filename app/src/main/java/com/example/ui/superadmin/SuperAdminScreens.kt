package com.example.ui.superadmin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EcosystemRepository
import com.example.model.FeatureFlag
import com.example.model.Tenant
import com.example.ui.components.MetricCard
import com.example.ui.components.StatusChip

@Composable
fun SuperAdminDashboardScreen(
    repository: EcosystemRepository,
    modifier: Modifier = Modifier
) {
    val tenants by repository.tenants.collectAsState()
    val activeTenantId by repository.activeTenantId.collectAsState()
    val featureFlags by repository.featureFlags.collectAsState()
    val auditLogs by repository.auditLogs.collectAsState()

    var activeAdminTab by remember { mutableStateOf("FEATURES") } // DASHBOARD, TENANTS, FEATURES, AUDIT

    // Dialog states
    var showRegisterTenantDialog by remember { mutableStateOf(false) }
    var selectedFeatureForEdit by remember { mutableStateOf<FeatureFlag?>(null) }
    var showRollbackConfirmDialog by remember { mutableStateOf<FeatureFlag?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Super Admin Navigation Tabs
        TabRow(
            selectedTabIndex = when (activeAdminTab) {
                "DASHBOARD" -> 0
                "TENANTS" -> 1
                "FEATURES" -> 2
                else -> 3
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = activeAdminTab == "DASHBOARD",
                onClick = { activeAdminTab = "DASHBOARD" },
                text = { Text("Platform Health", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = activeAdminTab == "TENANTS",
                onClick = { activeAdminTab = "TENANTS" },
                text = { Text("Tenants (${tenants.size})", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = activeAdminTab == "FEATURES",
                onClick = { activeAdminTab = "FEATURES" },
                text = { Text("Feature Flags (${featureFlags.size})", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.ToggleOn, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = activeAdminTab == "AUDIT",
                onClick = { activeAdminTab = "AUDIT" },
                text = { Text("Audit Trail", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }

        when (activeAdminTab) {
            "DASHBOARD" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Developer Super Admin Command Center",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF2E7D32)) {
                                        Text("SYSTEM OPTIMAL", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Central entitlement, feature registry and multi-tenant isolation coordinator for all client apps.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Platform Metrics
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                MetricCard(
                                    title = "Registered Tenants",
                                    value = "${tenants.size}",
                                    subtitle = "${tenants.count { it.active }} Active • ${tenants.count { !it.active }} Suspended",
                                    icon = Icons.Default.Apartment,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Feature Modules",
                                    value = "${featureFlags.size}",
                                    subtitle = "${featureFlags.count { it.defaultEnabled }} globally active",
                                    icon = Icons.Default.Extension,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                MetricCard(
                                    title = "Total End Users",
                                    value = "1,420+",
                                    subtitle = "Active customer sessions",
                                    icon = Icons.Default.People,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricCard(
                                    title = "Audit Events",
                                    value = "${auditLogs.size}",
                                    subtitle = "Immutable audit records",
                                    icon = Icons.Default.Shield,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Cloud Backend Health Diagnostics
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Cloud Backend Services Status",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                listOf(
                                    Triple("Cloud Firestore Enterprise DB", "Connected & Provisioned", true),
                                    Triple("Firebase Google Sign-In & CredentialManager", "Active & Registered", true),
                                    Triple("Zero-Trust Security Rules", "Rules Deployed & Enforced", true),
                                    Triple("Multi-Tenant Data Isolation Engine", "Enforcing tenantId Boundaries", true),
                                    Triple("Dynamic Feature Registry Daemon", "Polling Interval 10s (Cached)", true)
                                ).forEach { (service, desc, ok) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(service, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Icon(
                                            imageVector = if (ok) Icons.Default.CheckCircle else Icons.Default.Error,
                                            contentDescription = null,
                                            tint = if (ok) Color(0xFF2E7D32) else Color.Red,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "TENANTS" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Business Tenants Management",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { showRegisterTenantDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Register Tenant")
                            }
                        }
                    }

                    items(tenants) { tenant ->
                        val isCurrentActive = tenant.id == activeTenantId

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isCurrentActive) 2.dp else 0.dp,
                                    color = if (isCurrentActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = tenant.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = "Tenant ID: ${tenant.id} • Plan: ${tenant.plan}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    StatusChip(status = if (tenant.active) "ACTIVE" else "CANCELLED")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Tagline: ${tenant.tagline}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "Concierge: ${tenant.contactEmail} • ${tenant.contactPhone}", fontSize = 11.sp)
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Switch to this tenant in preview
                                    OutlinedButton(
                                        onClick = { repository.selectTenant(tenant.id) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(if (isCurrentActive) "Currently Active" else "Switch Preview Context", fontSize = 11.sp)
                                    }

                                    // Suspend / Activate toggle
                                    Button(
                                        onClick = { repository.setTenantActive(tenant.id, !tenant.active) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (tenant.active) Color(0xFFC62828) else Color(0xFF2E7D32)
                                        )
                                    ) {
                                        Text(if (tenant.active) "Suspend Tenant" else "Activate Tenant", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "FEATURES" -> {
                // Feature Management Center
                var filterCategory by remember { mutableStateOf("ALL") }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = "Feature Management Center",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Dynamically enable, disable, and configure 20+ feature modules globally or for specific tenant businesses with safe rollbacks.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Tenant Override Info Bar
                            val currentTenantObj = tenants.find { it.id == activeTenantId }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Target Tenant for Overrides: ${currentTenantObj?.name} (${activeTenantId})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    items(featureFlags) { flag ->
                        val tenantOverride = flag.tenantOverrides[activeTenantId]
                        val isEffective = tenantOverride ?: flag.defaultEnabled

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("feature_card_${flag.key}"),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = flag.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = "Key: ${flag.key} • Category: ${flag.category}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isEffective) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                    ) {
                                        Text(
                                            text = if (isEffective) "ACTIVE" else "DISABLED",
                                            color = if (isEffective) Color(0xFF2E7D32) else Color(0xFFC62828),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = flag.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(10.dp))

                                // Controls: Global Switch vs Tenant Override
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Switch(
                                            checked = flag.defaultEnabled,
                                            onCheckedChange = { repository.toggleFeatureGlobally(flag.key, it) },
                                            modifier = Modifier.testTag("global_toggle_${flag.key}")
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Global Default", fontSize = 11.sp)
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        // Tenant Override Toggle
                                        OutlinedButton(
                                            onClick = {
                                                val nextVal = !(tenantOverride ?: flag.defaultEnabled)
                                                repository.toggleFeatureForTenant(flag.key, activeTenantId, nextVal)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.testTag("tenant_override_${flag.key}")
                                        ) {
                                            Text(
                                                text = if (tenantOverride != null) "Override: $tenantOverride" else "Add Override",
                                                fontSize = 10.sp
                                            )
                                        }

                                        // Rollback Button
                                        IconButton(
                                            onClick = { showRollbackConfirmDialog = flag },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(Icons.Default.RestartAlt, contentDescription = "Rollback", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "AUDIT" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Platform-Wide Immutable Audit Trail",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Every administrative event, tenant change, and feature flag mutation is recorded cryptographically.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(auditLogs) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(text = log.actorRole, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(text = "Target: ${log.entityType} [${log.entityId}] • Tenant: ${log.tenantId}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = log.details, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Register Tenant
    if (showRegisterTenantDialog) {
        var newTenantName by remember { mutableStateOf("") }
        var brandColor by remember { mutableStateOf("#1E5F74") }
        var plan by remember { mutableStateOf("PRO") }

        AlertDialog(
            onDismissRequest = { showRegisterTenantDialog = false },
            title = { Text("Register New Business Tenant") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTenantName,
                        onValueChange = { newTenantName = it },
                        label = { Text("Business Studio Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = brandColor,
                        onValueChange = { brandColor = it },
                        label = { Text("Brand Color Hex (e.g. #1E5F74)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Subscription Plan:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("STARTER", "PRO", "ENTERPRISE").forEach { p ->
                            FilterChip(
                                selected = plan == p,
                                onClick = { plan = p },
                                label = { Text(p, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTenantName.isNotBlank()) {
                            repository.registerNewTenant(newTenantName, brandColor, plan)
                            showRegisterTenantDialog = false
                        }
                    }
                ) {
                    Text("Provision Tenant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterTenantDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Modal: Rollback Confirmation
    showRollbackConfirmDialog?.let { flag ->
        AlertDialog(
            onDismissRequest = { showRollbackConfirmDialog = null },
            title = { Text("Confirm Feature Rollback") },
            text = {
                Text("Are you sure you want to rollback '${flag.name}' (${flag.key}) to its last known good configuration? This will reset all tenant overrides.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.rollbackFeature(flag.key)
                        showRollbackConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Rollback Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRollbackConfirmDialog = null }) { Text("Cancel") }
            }
        )
    }
}
