package com.example.salah_app.ui.salah

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Duration

@Composable
fun Header(nextSalahName: String, timeToNextSalah: Duration) {
    val hours = timeToNextSalah.toHours()
    val minutes = timeToNextSalah.toMinutes() % 60

    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This single Text element replaces all three previous ones.
        Text(
            textAlign = TextAlign.Center,
            // buildAnnotatedString lets us mix and match styles.
            text = buildAnnotatedString {
                // Style for the numbers (hours and minutes)
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp, // A specific size for impact
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append(String.format("%d", hours))
                }

                // Style for the text labels ("hrs", "mins")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                ) {
                    append(" hrs ")
                }

                // Apply the number style again for minutes
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    append(String.format("%02d", minutes))
                }

                // Apply the label style again for the rest of the text
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                ) {
                    append(" mins until ")
                }

                // A special style for the prayer name
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append(nextSalahName)
                }
            }
        )
    }
}