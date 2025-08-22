package com.example.salah_app.ui.salah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salah_app.data.AyahData
import com.example.salah_app.data.Surah
import com.example.salah_app.ui.theme.AyahBrush
import com.example.salah_app.ui.theme.SalahAppTheme

@Composable
fun AyahOfTheDayCard(
    ayahData: AyahData,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent, // Let the background show through
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(AyahBrush)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ayah of the Day",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            // The verse itself
            Text(
                text = "\"${ayahData.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            // The source (e.g., "Surah Al-Fatihah, 1:1")
            Text(
                text = "Surah ${ayahData.surah.englishName}, ${ayahData.surah.number}:${ayahData.numberInSurah}",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Preview
@Composable
fun AyahOfTheDayCardPreview() {
    SalahAppTheme {
        AyahOfTheDayCard(
            ayahData = AyahData(
                text = "It is He who created the heavens and earth in truth.",
                surah = Surah(englishName = "Al-An'am", number = 6),
                numberInSurah = 73
            )
        )
    }
}