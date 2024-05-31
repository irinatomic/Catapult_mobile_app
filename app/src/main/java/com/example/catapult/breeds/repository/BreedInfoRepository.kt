package com.example.catapult.breeds.repository

import com.example.catapult.networking.api.BreedsApi
import com.example.catapult.data.database.CatapultDatabase
import com.example.catapult.data.database.entities.BreedDbModel
import com.example.catapult.data.mapper.asBreedDbModel
import com.example.catapult.networking.retrofit
import kotlinx.coroutines.flow.Flow

/**
 * Database is the single source of truth for breeds.
 * This repository is responsible for fetching breeds from the API and saving them to the database.
 * It also provides a way to observe all breeds from the database.
*/
object BreedInfoRepository {

    private val database by lazy { CatapultDatabase.database }
    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

    // API request -> save to db
    suspend fun fetchBreeds() {
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertAll(breeds.map { it.asBreedDbModel() })
    }

    suspend fun getBreeds(): List<BreedDbModel> {
        return database.breedDao().getAll()
    }

    /** Returns Flow which holds all breeds. */
    fun observeBreeds(): Flow<List<BreedDbModel>> {
        return database.breedDao().observeAll();
    }

    /** Returns Flow with BreedDbModel with given breedId. */
    fun observeBreedDetails(breedId: String): Flow<BreedDbModel?> {
        return database.breedDao().observeBreedById(breedId)
    }
}