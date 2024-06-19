package com.example.catapult.data.database.entities

import androidx.room.Entity

@Entity(
    tableName = "Leaderboard",
    primaryKeys = ["nickname", "result", "createdAt"]
)
data class LBItemDbModel (

    val nickname: String,
    val result: Double,
    val totalGamesPlayed: Int,
    val createdAt: Long
)