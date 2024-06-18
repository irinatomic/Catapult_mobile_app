package com.example.catapult.segments.leaderboard

import com.example.catapult.data.api.SubmitResultRequest
import com.example.catapult.data.database.AppDatabase
import com.example.catapult.data.database.entities.LBItemDbModel
import com.example.catapult.data.datastore.UserStore
import com.example.catapult.data.mapper.asLBItemDbModel
import com.example.catapult.networking.endpoints.LeaderboardApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase,
    private val store: UserStore
){

    suspend fun fetchLeaderboard(categoryId: Int = 1) {
        val lbItems = leaderboardApi.getLeaderboard(categoryId)
        database.leaderboardDao().deleteAll()
        database.leaderboardDao().insertAll(lbItems.map { it.asLBItemDbModel(lbItems) })
    }

    /**
     * We should submit the result to the API.
     * And change the last entry in the leaderboard table (published -> true)
     */
    suspend fun submitQuizResult(categoryId: Int = 1, result: Double) {
        withContext(Dispatchers.IO) {
            leaderboardApi.postLeaderboard(
                SubmitResultRequest(
                    nickname = store.getUserData().nickname,
                    result = result,
                    category = categoryId
                )
            )
        }

        // Retrieve the last entry in the leaderboard table
        val lastEntry = database.resultDao().getLastEntry()

        lastEntry.let {
            it.published = true
            database.resultDao().update(it)
        }
    }

    /** Returns Flow which holds all leaderboard items.
     * The items are sorted by result in descending order.
     * Does not have a categoryId since this app will support only 1 category of the quiz.
    */
    fun observeLeaderboard(): Flow<List<LBItemDbModel>> {
        return database.leaderboardDao().observeAll()
    }
}