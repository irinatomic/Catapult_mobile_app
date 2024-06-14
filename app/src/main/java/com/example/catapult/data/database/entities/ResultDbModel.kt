package com.example.catapult.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// This user's results in the game

@Entity
data class ResultDbModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val result: Float,
    val createdAt: Long,
    val published: Boolean
)