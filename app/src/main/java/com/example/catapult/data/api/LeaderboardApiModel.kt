package com.example.catapult.data.api

import kotlinx.serialization.Serializable

@Serializable
data class SubmitResultRequest(
    val nickname: String,
    val result: Double,
    val category: Int
)

@Serializable
data class SubmitResultResponse(
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