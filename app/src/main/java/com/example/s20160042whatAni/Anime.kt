package com.example.s20160042whatAni

data class Anime(
    val id: Int,
    val title: String,
    val maker: String,
    val rating: Int,
    val poster: String,
    val director: String?,
    val story: String?,
    val makerlogo: String,
    val trailer: String
)
