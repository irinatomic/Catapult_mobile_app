package com.example.catapult.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

@Serializable
data class Image(
    @SerialName("url") val imageUrl: String
)

@Serializable
data class BreedApiModel(
    val id: String,
    val name: String? = "",
    @SerialName("alt_names")
    val altNames: String? = "",
    val description: String? = "",
    val temperament: String? = "",
    val origin: String? = "",
    @SerialName("life_span")
    val lifeSpan: String? = "",
    val weight: Weight? = null,
    val adaptability: Int? = 0,
    @SerialName("affection_level")
    val affectionLevel: Int? = 0,
    @SerialName("child_friendly")
    val childFriendly: Int? = 0,
    val intelligence: Int,
    @SerialName("shedding_level")
    val sheddingLevel: Int? = 0,
    @SerialName("stranger_friendly")
    val strangerFriendly: Int? = 0,
    val rare: Int? = -1,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String = "",
    val image: Image? = null
)