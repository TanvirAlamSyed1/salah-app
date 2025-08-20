package com.example.salah_app.ui.salah

import android.R
import android.graphics.Color
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.salah_app.ui.theme.IshaBrush
import com.example.salah_app.ui.theme.SalahAppTheme

@Composable
fun SalahTimeClock(
    salahName: String,
    salahTime: String,
    salahDescription: String,
    salahBenefits: String,
    brush: Brush, // 1. Add Brush as a parameter
    modifier: Modifier = Modifier
) {
    var isExpandable by remember { mutableStateOf(false) }

    Surface(
        color = androidx.compose.ui.graphics.Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        // Use a Box to apply the gradient background behind the content
        Box(
            modifier = Modifier
                .background(brush) // 2. Apply the gradient here
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
                        style = MaterialTheme.typography.titleLarge,
                        color = androidx.compose.ui.graphics.Color.White,

                    )

                    // 2. This Spacer pushes the time to the far right
                    Spacer(modifier = Modifier.weight(1f))

                    // The prayer time on the right
                    Text(
                        text = salahTime,
                        style = MaterialTheme.typography.headlineSmall, // Made the text bigger
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f) // Slightly transparent
                    )
                }

                // 4. The expandable content is now INSIDE the Column
                if (isExpandable) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color) // A visual separator
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = salahDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = salahBenefits,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                    )
                }
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
            brush = IshaBrush,
            salahBenefits = "It is said to wash away sins and bring peace to the mind before sleep."
        )
    }
}