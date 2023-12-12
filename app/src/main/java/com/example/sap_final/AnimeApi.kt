package com.example.sap_final

import retrofit2.http.GET

interface AnimeApi {
    @GET("anime")
    suspend fun getAnimes(): List<Anime>
}