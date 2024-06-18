package com.example.catapult.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Breeds")
data class BreedDbModel(

    @PrimaryKey val id: String,
    val name: String,
    val altNames: String,
    val description: String,
    val temperament: String,
    val origin: String,
    val lifeSpan: String,
    val weightImperial: String,
    val weightMetric: String,

    // traits
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val intelligence: Int,
    val sheddingLevel: Int,
    val strangerFriendly: Int,
    val rare: Int,

    val wikipediaUrl: String,
    val imageUrl: String
) {
    fun averageWeightMetric(): Double {
        // "3 - 7 kg"
        val weights = weightMetric.replace(" kg", "").split(" - ").map { it.toFloat() }
        return weights.average()
    }

    fun averageWeightImperial(): Double {
        // "7 - 15 lb"
        val weights = weightImperial.replace(" lb", "").split(" - ").map { it.toFloat() }
        return weights.average()
    }
}
