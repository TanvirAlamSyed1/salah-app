package com.example.salah_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.salah_app.ui.salah.SalahScreen
import com.example.salah_app.ui.theme.SalahAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // In MainActivity.kt
        setContent {
            SalahAppTheme {
                SalahScreen()
            }
        }
    }
}