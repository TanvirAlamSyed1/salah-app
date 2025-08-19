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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


sealed interface SalahUiState {
    data object Loading : SalahUiState
    // Add city and country properties to the Success state
    data class Success(
        val timings: Timings,
        val city: String,
        val country: String
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
                // Pass the city and country along with the timings
                _uiState.update { SalahUiState.Success(timings, city, country) }
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