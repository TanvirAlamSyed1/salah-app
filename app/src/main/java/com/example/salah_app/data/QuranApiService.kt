package com.example.salah_app.data

import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    @GET("v1/ayah/{number}/en.asad")
    suspend fun getRandomAyah(
        @Path("number") number: Int
    ): AyahResponse
}