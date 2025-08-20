package com.example.salah_app.ui.salah

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salah_app.data.Prayer
import com.example.salah_app.data.SalahRepository
import com.example.salah_app.data.Timings
import com.example.salah_app.ui.theme.SalahAppTheme
import com.google.android.gms.location.LocationServices

@Composable
fun SalahScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext
    val factory = remember { SalahViewModelFactory(context as Application, SalahRepository()) }
    val salahViewModel: SalahViewModel = viewModel(factory = factory)

    val uiState by salahViewModel.uiState.collectAsStateWithLifecycle()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            salahViewModel.fetchPrayerTimesForLocation(it)
                        } ?: salahViewModel.fetchPrayerTimesForDefaultLocation()
                    }
                }
            } else {
                salahViewModel.fetchPrayerTimesForDefaultLocation()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is SalahUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is SalahUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is SalahUiState.Success -> {
                    // Create a list of prayer information
                    val prayers = remember(state.timings) {
                        listOf(
                            Prayer(
                                "Fajr",
                                "The dawn prayer, performed before sunrise.",
                                "Starts the day with spiritual reflection and is said to be worth more than the world and everything in it."
                            ),
                            Prayer("Dhuhr", "The midday prayer, performed after the sun has passed its zenith.", "Offers a break from daily work to remember God and seek guidance."),
                            Prayer("Asr",  "The afternoon prayer, performed late in the afternoon.", "Considered a crucial prayer that signifies patience and perseverance."),
                            Prayer("Maghrib", "The sunset prayer, performed just after sunset.", "Marks the end of the day and is a time for gratitude and seeking forgiveness."),
                            Prayer("Isha", "The night prayer, performed after twilight has disappeared.", "Brings peace before sleep and is highly rewarded.")
                        )
                    }

                    val timings = state.timings
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Today's Prayer Times",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "${state.city}, ${state.country}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        PrayerTimeCards(prayers = prayers ,timings = state.timings)
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerTimeCards(prayers: List<Prayer>, timings: Timings) {
    val prayerTimes = mapOf(
        "Fajr" to timings.fajr, "Dhuhr" to timings.dhuhr, "Asr" to timings.asr,
        "Maghrib" to timings.maghrib, "Isha" to timings.isha
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