package com.example.catapult.networking.endpoints

import com.example.catapult.data.api.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") categoryId: Int,
    ): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postLeaderboard(
        @Body leaderboard: SubmitResultRequest
    ): SubmitResultResponse
}