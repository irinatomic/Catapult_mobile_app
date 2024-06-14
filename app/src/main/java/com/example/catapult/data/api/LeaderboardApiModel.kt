package com.example.catapult.data.api

import kotlinx.serialization.Serializable

@Serializable
data class PostLeaderboardResponse(
    val result: LeaderboardApiModel,
    val ranking: Int
)

@Serializable
data class LeaderboardApiModel(
    val category: Int = 0,
    val nickname: String = "",
    val result: Float = 0.0f,
    val createdAt: Long = 0L        // timestamp
)