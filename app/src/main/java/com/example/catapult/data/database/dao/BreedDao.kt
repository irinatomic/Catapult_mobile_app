package com.example.catapult.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.database.entities.BreedDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(breedDbModel: BreedDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<BreedDbModel>)

    @Query("SELECT * FROM BreedDbModel")
    suspend fun getAll(): List<BreedDbModel>

    suspend fun getCountOfBreeds(): Int {
        return getAll().size
    }

    @Query("SELECT * FROM BreedDbModel")
    fun observeAll(): Flow<List<BreedDbModel>>

    @Query("SELECT * FROM BreedDbModel WHERE id = :breedId")
    fun observeBreedById(breedId: String): Flow<BreedDbModel?>

    suspend fun getAllTemperaments(): List<String> {
        return getAll()
            .asSequence()
            .map { it.temperament.split(",") }
            .flatten()
            .map { it.trim() }
            .map{ it.lowercase() }
            .distinct()
            .toList()
    }
}