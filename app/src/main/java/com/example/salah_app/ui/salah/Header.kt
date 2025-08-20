package com.example.salah_app.ui.salah

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Duration

@Composable
fun Header(nextSalahName: String, timeToNextSalah: Duration) {
    val hours = timeToNextSalah.toHours()
    val minutes = timeToNextSalah.toMinutes() % 60

    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Next prayer:",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = nextSalahName,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = String.format("%d hrs %02d mins until", hours, minutes),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Light
        )
    }
}