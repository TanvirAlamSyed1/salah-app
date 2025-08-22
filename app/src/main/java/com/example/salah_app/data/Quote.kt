package com.example.salah_app.data

/**
 * This is the top-level response from the API.
 * It contains a 'data' object.
 */
data class AyahResponse(
    val data: AyahData
)

/**
 * This represents the 'data' object in the JSON.
 * It holds the main content of the verse and information about its source (the Surah).
 */
data class AyahData(
    val text: String,           // This is the English translation of the verse
    val surah: Surah,           // A nested object containing Surah details
    val numberInSurah: Int      // The verse number within the Surah
)

/**
 * This represents the 'surah' object, giving context to the verse.
 */
data class Surah(
    val englishName: String,    // The English name of the Surah (e.g., "Al-Fatihah")
    val number: Int             // The number of the Surah (e.g., 1)
)