package com.example.salah_app.ui.salah

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.salah_app.data.Prayer
import com.example.salah_app.data.Timings
import com.example.salah_app.ui.theme.*

@Composable
fun PrayerTimeCards(prayers: List<Prayer>, timings: Timings) {
    val prayerTimes = mapOf(
        "Fajr" to timings.fajr, "Dhuhr" to timings.dhuhr, "Asr" to timings.asr,
        "Maghrib" to timings.maghrib, "Isha" to timings.isha
    )
    val prayerBrushes = mapOf(
        "Fajr" to FajrBrush, "Dhuhr" to DhuhrBrush, "Asr" to AsrBrush,
        "Maghrib" to MaghribBrush, "Isha" to IshaBrush
    )
    var expandedCardName by remember { mutableStateOf<String?>(null) }

    // 1. Replace Column with a LazyRow for horizontal scrolling.
    LazyRow(
        // 2. Add some horizontal padding and space between items.
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // 3. Use the 'items' block, which is the standard for lazy lists.
        items(prayers) { prayer ->
            SalahTimeClock(
                salahName = prayer.name,
                salahTime = prayerTimes[prayer.name] ?: "",
                salahDescription = prayer.description,
                salahBenefits = prayer.benefits,
                brush = prayerBrushes[prayer.name] ?: FajrBrush,

                // 2. Tell the card if it should be expanded.
                isExpanded = (expandedCardName == prayer.name),

                // 3. Tell the card what to do when clicked.
                onClick = {
                    expandedCardName = if (expandedCardName == prayer.name) {
                        null // If it's already expanded, collapse it.
                    } else {
                        prayer.name // Otherwise, expand this card.
                    }
                },
                modifier = Modifier.width(320.dp)
            )
        }
    }
}