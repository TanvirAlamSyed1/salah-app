package com.example.salah_app.ui.salah

// 1. Add these new imports for the snapping behavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
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

    // 2. Create a state for the LazyRow to track its scroll position
    val lazyListState = rememberLazyListState()

    // 3. Create the snapping behavior using the list state
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp), // A little less space looks cleaner
        contentPadding = PaddingValues(horizontal = 24.dp), // A bit more padding to center the card

        // 4. Pass the state and the new fling behavior to the LazyRow
        state = lazyListState,
        flingBehavior = flingBehavior
    ) {
        items(prayers) { prayer ->
            SalahTimeClock(
                salahName = prayer.name,
                salahTime = prayerTimes[prayer.name] ?: "",
                salahDescription = prayer.description,
                salahBenefits = prayer.benefits,
                brush = prayerBrushes[prayer.name] ?: FajrBrush,
                isExpanded = (expandedCardName == prayer.name),
                onClick = {
                    expandedCardName = if (expandedCardName == prayer.name) {
                        null
                    } else {
                        prayer.name
                    }
                },
                modifier = Modifier.width(340.dp) // Adjusted width to fit padding
            )
        }
    }
}