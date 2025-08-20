package com.example.salah_app.ui.salah

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.salah_app.data.SalahRepository
import com.example.salah_app.data.Timings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter


sealed interface SalahUiState {
    data object Loading : SalahUiState
    // Add city and country properties to the Success state
    data class Success(
        val timings: Timings,
        val city: String,
        val country: String,
        val nextSalahName: String, // New: Name of the next prayer
        val timeToNextSalah: Duration, // New: Time remaining
        val currentTime: LocalTime // New: The current system time
    ) : SalahUiState
    data class Error(val message: String) : SalahUiState
}

class SalahViewModel(
    application: Application,
    private val salahRepository: SalahRepository
) : AndroidViewModel(application) {

    private companion object {
        const val DEFAULT_CITY = "Denton"
        const val DEFAULT_COUNTRY = "GB"
    }

    private val _uiState = MutableStateFlow<SalahUiState>(SalahUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        // Start the timer when the ViewModel is created
        startTimer()
    }

    // This is the main timer that runs every second
    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                // Check if the current state is Success
                val currentState = _uiState.value
                if (currentState is SalahUiState.Success) {
                    val now = LocalTime.now()
                    // Recalculate the next prayer based on the new time
                    val (nextSalahName, timeToNextSalah) = findNextSalah(now, currentState.timings)
                    // Update the state with the new time information
                    _uiState.update {
                        currentState.copy(
                            currentTime = now,
                            nextSalahName = nextSalahName,
                            timeToNextSalah = timeToNextSalah
                        )
                    }
                }
                delay(1000L) // Wait for 1 second
            }
        }
    }

    // Helper function to find the next prayer and time remaining
    private fun findNextSalah(currentTime: LocalTime, timings: Timings): Pair<String, Duration> {
        val prayerTimeMap = mapOf(
            "Fajr" to LocalTime.parse(timings.fajr, DateTimeFormatter.ofPattern("HH:mm")),
            "Dhuhr" to LocalTime.parse(timings.dhuhr, DateTimeFormatter.ofPattern("HH:mm")),
            "Asr" to LocalTime.parse(timings.asr, DateTimeFormatter.ofPattern("HH:mm")),
            "Maghrib" to LocalTime.parse(timings.maghrib, DateTimeFormatter.ofPattern("HH:mm")),
            "Isha" to LocalTime.parse(timings.isha, DateTimeFormatter.ofPattern("HH:mm"))
        )

        val nextPrayer = prayerTimeMap.entries.firstOrNull { (_, time) ->
            currentTime.isBefore(time)
        }

        return if (nextPrayer != null) {
            // If the next prayer is today
            val duration = Duration.between(currentTime, nextPrayer.value)
            Pair(nextPrayer.key, duration)
        } else {
            // If the next prayer is Fajr tomorrow
            val fajrTime = prayerTimeMap["Fajr"]!!
            val duration = Duration.between(currentTime, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, fajrTime))
            Pair("Fajr", duration)
        }
    }

    fun fetchPrayerTimesForLocation(location: Location) {
        viewModelScope.launch {
            _uiState.update { SalahUiState.Loading }
            val address: Address? = try {
                val geocoder = Geocoder(getApplication())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.awaitGetFromLocation(location.latitude, location.longitude)
                } else {
                    withContext(Dispatchers.IO) {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            ?.firstOrNull()
                    }
                }
            } catch (e: Exception) {
                null
            }
            val city = address?.locality ?: DEFAULT_CITY
            val country = address?.countryCode ?: DEFAULT_COUNTRY
            getTimingsAndUpdateState(city, country)
        }
    }

    // This is the function that was missing from your file
    fun fetchPrayerTimesForDefaultLocation() {
        viewModelScope.launch {
            _uiState.update { SalahUiState.Loading }
            getTimingsAndUpdateState(DEFAULT_CITY, DEFAULT_COUNTRY)
        }
    }

    // Inside your SalahViewModel class

    // Modify the private helper function
    private suspend fun getTimingsAndUpdateState(city: String, country: String) {
        salahRepository.getPrayerTimes(city, country)
            .onSuccess { timings ->
                // Now, when we get the timings, we do an initial calculation
                val now = LocalTime.now()
                val (nextSalahName, timeToNextSalah) = findNextSalah(now, timings)
                _uiState.update {
                    SalahUiState.Success(timings, city, country, nextSalahName, timeToNextSalah, now)
                }
            }
            .onFailure { error ->
                _uiState.update { SalahUiState.Error(error.message ?: "Unknown error") }
            }
    }

    private suspend fun Geocoder.awaitGetFromLocation(latitude: Double, longitude: Double): Address? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return null
        return suspendCoroutine { continuation ->
            getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    continuation.resume(addresses.firstOrNull())
                }
                override fun onError(errorMessage: String?) {
                    continuation.resume(null)
                }
            })
        }
    }
}