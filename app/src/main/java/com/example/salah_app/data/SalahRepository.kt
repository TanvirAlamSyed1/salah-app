package com.example.salah_app.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SalahRepository {

    // Set up a singleton Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.aladhan.com/")
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()

    private val salahApiService = retrofit.create(SalahApiService::class.java)

    // Function to get data, handling potential network errors
    suspend fun getPrayerTimes(city: String, country: String): Result<Timings> {
        return try {
            val response = salahApiService.getPrayerTimes(city, country)
            Result.success(response.data.timings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}