package com.example.salah_app.ui.salah

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salah_app.ui.theme.IshaBrush
import com.example.salah_app.ui.theme.SalahAppTheme


// 1. The function signature has changed. It no longer manages its own state.
@Composable
fun SalahTimeClock(
    salahName: String,
    salahTime: String,
    salahDescription: String,
    salahBenefits: String,
    brush: Brush,
    isExpanded: Boolean,      // <-- State is passed in
    onClick: () -> Unit,      // <-- Event is passed in
    modifier: Modifier = Modifier
) {
    // 2. Animate the rotation of the arrow icon
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "rotation")

    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Box(modifier = Modifier.background(brush)) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .clickable(onClick = onClick) // <-- Use the passed-in click event
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = salahName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = salahTime,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    // 3. Add the animated icon to the end of the row
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand or collapse card",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .rotate(rotationAngle)
                    )
                }

                // 4. The expandable content now checks the passed-in 'isExpanded' state
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = salahDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = salahBenefits,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}