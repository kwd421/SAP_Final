package com.example.s20160042whatAni

import retrofit2.http.GET

interface AnimeApi {
    @GET("anime")
    suspend fun getAnimes(): List<Anime>
}