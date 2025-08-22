package com.example.salah_app.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SalahRepository {

    // Common Moshi instance for JSON parsing
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // 1. Service for Prayer Times (Adhan API)
    private val adhanApiService: SalahApiService = Retrofit.Builder()
        .baseUrl("https://api.aladhan.com/") // Prayer times URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SalahApiService::class.java)

    // 2. Service for Quran Verses (Al-Quran Cloud API)
    private val quranApiService: QuranApiService = Retrofit.Builder()
        .baseUrl("https://api.alquran.cloud/") // Quran verse URL
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(QuranApiService::class.java)


    // This function now correctly uses the Adhan service
    suspend fun getPrayerTimes(city: String, country: String): Result<Timings> {
        return try {
            val response = adhanApiService.getPrayerTimes(city, country)
            Result.success(response.data.timings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This function now correctly uses the Quran service
    suspend fun getRandomAyah(): Result<AyahData> {
        return try {
            // Generate a random verse number (1 to 6236)
            val randomAyahNumber = (1..6236).random()
            val response = quranApiService.getRandomAyah(randomAyahNumber)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}