package com.example.sap_final

data class Anime(
    val id: Int,
    val title: String,
    val maker: String,
    val rating: Int,
    val url: String,
    val director: String?,
    val story: String?,
    val makerlogo: String
)
