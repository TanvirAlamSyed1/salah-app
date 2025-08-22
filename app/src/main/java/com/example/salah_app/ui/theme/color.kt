package com.example.salah_app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val FajrBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFA726), Color(0xFFFFE082)) // Orange to Yellow
)
val DhuhrBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF42A5F5), Color(0xFF90CAF9)) // Light Blue to Blue
)
val AsrBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF29B6F6), Color(0xFFFFF59D)) // Blue to Light Yellow
)
val MaghribBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF5E35B1), Color(0xFF311B92)) // Dark Blue to Purple
)
val IshaBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF4527A0), Color.Black)       // Dark Purple to Black
)


// A warm, sunrise-themed gradient
val ScreenBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF3B4371), Color(0xFFF3904F))
)

val AyahBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF26A69A), Color(0xFF80CBC4)) // Teal gradient
)