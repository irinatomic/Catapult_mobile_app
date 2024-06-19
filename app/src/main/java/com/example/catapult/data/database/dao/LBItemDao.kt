package com.example.catapult.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.database.entities.LBItemDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LBItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<LBItemDbModel>)

    @Query("SELECT * FROM Leaderboard ORDER BY result DESC")
    suspend fun getAll(): List<LBItemDbModel>

    @Query("SELECT * FROM Leaderboard ORDER BY result DESC")
    fun observeAll(): Flow<List<LBItemDbModel>>

    @Query("SELECT * FROM Leaderboard ORDER BY result DESC LIMIT 10")
    suspend fun getTop10(): List<LBItemDbModel>

    /**
     * Returns a pair: (rank, result)
     */
    suspend fun getBestGlobalRank(nickname: String): Pair<Int, Double> {
        val allResults = getAll()
        val userResult = allResults.find { it.nickname == nickname }

        val rank = allResults.indexOf(userResult) + 1
        val score = userResult?.result ?: -1.0
        return Pair(rank, score)
    }

    @Query("DELETE FROM Leaderboard")
    suspend fun deleteAll()
}