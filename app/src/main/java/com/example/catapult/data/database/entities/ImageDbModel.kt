package com.example.catapult.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDbModel (

    @PrimaryKey val id: Int,
    val url: String,
    val breedId: Int,
    val width: Int,
    val height: Int,
) {
    init {
        require(url.isNotBlank()) { "URL must not be blank" }
    }
}