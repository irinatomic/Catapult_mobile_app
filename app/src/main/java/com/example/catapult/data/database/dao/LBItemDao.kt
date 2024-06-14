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

    @Query("SELECT * FROM LBItemDbModel ORDER BY result DESC")
    suspend fun getAll(): List<LBItemDbModel>

    @Query("SELECT * FROM LBItemDbModel ORDER BY result DESC")
    fun observeAll(): Flow<List<LBItemDbModel>>

    @Query("SELECT * FROM LBItemDbModel ORDER BY result DESC LIMIT 10")
    suspend fun getTop10(): List<LBItemDbModel>

    suspend fun findUserPosition(nickname: String): Int {
        val allResults = getAll()
        val userResult = allResults.find { it.nickname == nickname }
        return allResults.indexOf(userResult)
    }

    @Query("DELETE FROM LBItemDbModel")
    suspend fun deleteAll()
}