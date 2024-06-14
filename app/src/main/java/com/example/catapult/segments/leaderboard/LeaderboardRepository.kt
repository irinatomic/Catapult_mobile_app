package com.example.catapult.segments.leaderboard

import com.example.catapult.data.database.AppDatabase
import com.example.catapult.data.database.entities.LBItemDbModel
import com.example.catapult.data.mapper.asLBItemDbModel
import com.example.catapult.networking.endpoints.LeaderboardApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase
){

    suspend fun fetchLeaderboard(categoryId: Int = 1) {
        val lbItems = leaderboardApi.getLeaderboard(categoryId)
        database.leaderboardDao().deleteAll()
        database.leaderboardDao().insertAll(lbItems.map { it.asLBItemDbModel(lbItems) })
    }

    /** Returns Flow which holds all leaderboard items.
     * The items are sorted by result in descending order.
     * Does not have a categoryId since this app will support only 1 category of the quiz.
    */
    fun observeLeaderboard(): Flow<List<LBItemDbModel>> {
        return database.leaderboardDao().observeAll()
    }
}