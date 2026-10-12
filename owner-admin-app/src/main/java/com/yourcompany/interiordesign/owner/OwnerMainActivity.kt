package com.yourcompany.interiordesign.owner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class OwnerMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    OwnerAppStandaloneRoot()
                }
            }
        }
    }
}

@Composable
fun OwnerAppStandaloneRoot() {
    Text("Interior Business Manager - Standalone Owner Admin App")
}
