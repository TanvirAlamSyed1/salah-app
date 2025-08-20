package com.example.salah_app.ui.salah

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salah_app.data.Prayer
import com.example.salah_app.data.Timings
import com.example.salah_app.ui.theme.AsrBrush
import com.example.salah_app.ui.theme.DhuhrBrush
import com.example.salah_app.ui.theme.FajrBrush
import com.example.salah_app.ui.theme.IshaBrush
import com.example.salah_app.ui.theme.MaghribBrush
import com.example.salah_app.ui.theme.SalahAppTheme
import kotlin.collections.forEach


@Composable
fun PrayerTimeCards(prayers: List<Prayer>, timings: Timings) {
    val prayerTimes = mapOf(
        "Fajr" to timings.fajr, "Dhuhr" to timings.dhuhr, "Asr" to timings.asr,
        "Maghrib" to timings.maghrib, "Isha" to timings.isha
    )
    val prayerBrushes = mapOf(
        "Fajr" to FajrBrush,
        "Dhuhr" to DhuhrBrush,
        "Asr" to AsrBrush,
        "Maghrib" to MaghribBrush,
        "Isha" to IshaBrush
    )

    // This Column will now arrange all cards vertically, one underneath another.
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp), // A bit more space between cards
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Loop through all prayers and create one card for each.
        prayers.forEach { prayer ->
            SalahTimeClock(
                salahName = prayer.name,
                salahTime = prayerTimes[prayer.name] ?: "",
                salahDescription = prayer.description,
                salahBenefits = prayer.benefits,
                brush = prayerBrushes[prayer.name] ?: FajrBrush,
                // Make each card take the full width of the screen.
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun SalahScreenPreview() {
    SalahAppTheme {
        // 1. Create fake data for BOTH timings and the list of prayer details
        val fakeTimings = Timings("04:30", "13:15", "17:00", "20:30", "22:00")
        val fakePrayers = listOf(
            Prayer(
                "Fajr",
                "The dawn prayer, performed before sunrise.",
                "Starts the day with spiritual reflection."
            ),
            Prayer(
                "Dhuhr",
                "The midday prayer, after the sun has passed its zenith.",
                "Offers a break from daily work to remember God."
            ),
            Prayer(
                "Asr",
                "The afternoon prayer, performed late in the afternoon.",
                "A crucial prayer signifying patience."
            ),
            Prayer(
                "Maghrib",
                "The sunset prayer, performed just after sunset.",
                "A time for gratitude and seeking forgiveness."
            ),
            Prayer(
                "Isha",
                "The night prayer, after twilight has disappeared.",
                "Brings peace before sleep."
            )
        )

        // The preview UI remains the same
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Today's Prayer Times",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Denton, GB",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )
                }
                // 2. Pass BOTH pieces of fake data to the updated function
                PrayerTimeCards(prayers = fakePrayers, timings = fakeTimings)
            }
        }
    }
}