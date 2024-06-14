package com.example.catapult.data.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["nickname", "result", "createdAt"])
data class LBItemDbModel (

    val nickname: String,
    val result: Float,
    val totalGamesPlayed: Int,
    val createdAt: Long
)