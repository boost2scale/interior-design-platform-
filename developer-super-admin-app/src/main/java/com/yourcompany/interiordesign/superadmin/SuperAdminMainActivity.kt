package com.yourcompany.interiordesign.superadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class SuperAdminMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    SuperAdminStandaloneRoot()
                }
            }
        }
    }
}

@Composable
fun SuperAdminStandaloneRoot() {
    Text("Platform Super Admin - Standalone Super Admin App")
}
