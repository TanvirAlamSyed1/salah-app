package com.example.salah_app.ui.salah

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salah_app.ui.theme.SalahAppTheme

@Composable
fun SalahTimeClock(
    salahName: String,
    salahTime: String,
    salahDescription: String,
    salahBenefits: String,
    modifier: Modifier = Modifier
) {
    var isExpandable by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .clickable { isExpandable = !isExpandable }
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 1. THIS ROW IS THE MAIN CHANGE
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // Aligns name and time nicely
            ) {
                // The prayer name on the left
                Text(
                    text = salahName,
                    style = MaterialTheme.typography.titleLarge, // Made the text bigger
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 2. This Spacer pushes the time to the far right
                Spacer(modifier = Modifier.weight(1f))

                // The prayer time on the right
                Text(
                    text = salahTime,
                    style = MaterialTheme.typography.headlineSmall, // Made the text bigger
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 4. The expandable content is now INSIDE the Column
            if (isExpandable) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider() // A visual separator
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = salahDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = salahBenefits,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun SalahTimeClockPreview() {
    SalahAppTheme {
        SalahTimeClock(
            salahName = "Isha",
            salahTime = "21:45",
            salahDescription = "The night prayer, performed after twilight has disappeared.",
            salahBenefits = "It is said to wash away sins and bring peace to the mind before sleep."
        )
    }
}