package com.example.salah_app.ui.salah

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salah_app.data.Prayer
import com.example.salah_app.data.SalahRepository
import com.example.salah_app.data.Timings
import com.google.android.gms.location.LocationServices
import java.time.Duration

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

    Scaffold(modifier = modifier.fillMaxSize(),  containerColor = Color.Transparent) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    // 2. The Prayer data creation is now correct
                    val prayers = remember(state.timings) {
                        listOf(
                            Prayer("Fajr", "The dawn prayer, performed before sunrise.", "Starts the day with spiritual reflection and is said to be worth more than the world and everything in it."),
                            Prayer("Dhuhr", "The midday prayer, performed after the sun has passed its zenith.", "Offers a break from daily work to remember God and seek guidance."),
                            Prayer("Asr", "The afternoon prayer, performed late in the afternoon.", "Considered a crucial prayer that signifies patience and perseverance."),
                            Prayer("Maghrib", "The sunset prayer, performed just after sunset.", "Marks the end of the day and is a time for gratitude and seeking forgiveness."),
                            Prayer("Isha", "The night prayer, performed after twilight has disappeared.", "Brings peace before sleep and is highly rewarded.")
                        )
                    }
                    val scrollState = rememberScrollState()

                    // 3. All content must be inside ONE parent layout, like this scrollable Column
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Salah Times",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Header(
                            nextSalahName = state.nextSalahName,
                            timeToNextSalah = state.timeToNextSalah
                        )
                        Text(
                            text = "${state.city}, ${state.country}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal
                        )

                        PrayerTimeCards(prayers = prayers, timings = state.timings)
                    }
                }
            }
        }
    }
}