package com.example.salah_app.data

import retrofit2.http.GET
import retrofit2.http.Query

interface SalahApiService {
    // Defines a GET request to the "timingsByCity" endpoint
    @GET("v1/timingsByCity")
    suspend fun getPrayerTimes(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int = 8 // Using the "Muslim World League" calculation method
    ): PrayerTimesResponse
}