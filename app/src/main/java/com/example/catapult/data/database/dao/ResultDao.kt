package com.example.catapult.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.database.entities.ResultDbModel

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(result: ResultDbModel)

    @Query("SELECT * FROM ResultDbModel ORDER BY createdAt DESC")
    suspend fun getAll(): List<ResultDbModel>
}