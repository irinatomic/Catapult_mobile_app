package com.example.catapult.data.api

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)