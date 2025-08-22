package com.example.salah_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.salah_app.ui.salah.SalahScreen
import com.example.salah_app.ui.theme.SalahAppTheme
import com.example.salah_app.ui.theme.ScreenBackgroundBrush

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalahAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ScreenBackgroundBrush)
                ) {
                    // Call the MainScreen which contains all your navigation logic
                    SalahScreen()
                }
            }
        }
    }
}