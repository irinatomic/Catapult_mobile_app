package com.example.catapult.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.database.entities.ImageDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageDbModel: ImageDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ImageDbModel>)

    @Query("SELECT * FROM Images WHERE breedId = :breedId")
    suspend fun getAllForBreed(breedId: String): List<ImageDbModel>

    @Query("SELECT * FROM Images WHERE breedId = :breedId")
    fun observeAllForBreed(breedId: String): Flow<List<ImageDbModel>>

    // In ImageDao.kt
    @Query("SELECT COUNT(*) FROM Images WHERE breedId = :breedId")
    suspend fun countImagesForBreed(breedId: String): Int
}