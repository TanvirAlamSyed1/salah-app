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
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
                        PrayerTimeCards(timings = state.timings)
                    }
                }
            }
        }
    }
}

@Composable
private fun PrayerTimeCards(timings: Timings) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SalahTimeClock(salahName = "Fajr", salahTime = timings.fajr, modifier = Modifier.weight(1f))
            SalahTimeClock(salahName = "Dhuhr", salahTime = timings.dhuhr, modifier = Modifier.weight(1f))
            SalahTimeClock(salahName = "Asr", salahTime = timings.asr, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.67f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SalahTimeClock(salahName = "Maghrib", salahTime = timings.maghrib, modifier = Modifier.weight(1f))
            SalahTimeClock(salahName = "Isha", salahTime = timings.isha, modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalahScreenPreview() {
    SalahAppTheme {
        val fakeTimings = Timings("04:30", "13:15", "17:00", "20:30", "22:00")

        // This preview now accurately shows the success UI
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
                PrayerTimeCards(timings = fakeTimings)
            }
        }
    }
}