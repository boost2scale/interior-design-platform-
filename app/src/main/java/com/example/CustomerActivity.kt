package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.auth.AuthManager
import com.example.data.EcosystemRepository
import com.example.model.EcosystemApp
import com.example.ui.theme.MyApplicationTheme

class CustomerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authManager = AuthManager(this)
        val repository = EcosystemRepository(this).apply {
            selectApp(EcosystemApp.CUSTOMER_APP)
        }
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
