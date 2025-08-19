package com.example.salah_app.data
import com.squareup.moshi.Json

data class PrayerTimesResponse(
    @Json(name = "data") val data: TimingsData
)

// Represents the "data" object which contains the timings
data class TimingsData(
    @Json(name = "timings") val timings: Timings
)

// Represents the "timings" object with each prayer time
data class Timings(
    @Json(name = "Fajr") val fajr: String,
    @Json(name = "Dhuhr") val dhuhr: String,
    @Json(name = "Asr") val asr: String,
    @Json(name = "Maghrib") val maghrib: String,
    @Json(name = "Isha") val isha: String
)