package com.example.catapult.segments.breeds

import com.example.catapult.data.database.AppDatabase
import com.example.catapult.networking.endpoints.BreedsApi
import com.example.catapult.data.database.entities.BreedDbModel
import com.example.catapult.data.database.entities.ImageDbModel
import com.example.catapult.data.mapper.asBreedDbModel
import com.example.catapult.data.mapper.asImageDbModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Database is the single source of truth for breeds.
 * This repository is responsible for fetching breeds from the API and saving them to the database.
 * It also provides a way to observe all breeds from the database.
*/

class BreedRepository @Inject constructor(
    private val database: AppDatabase,
    private val breedsApi: BreedsApi
) {

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
        return database.breedDao().observeAll()
    }

    /** Returns Flow with BreedDbModel with given breedId. */
    fun observeBreedDetails(breedId: String): Flow<BreedDbModel?> {
        return database.breedDao().observeBreedById(breedId)
    }

    /** Fetches images for breed and saves them to the database. */
    suspend fun fetchImagesForBreed(breedId: String) {
        // Check if there are any images for the breed in the database
        val imageCount = database.imageDao().countImagesForBreed(breedId)

        if(imageCount == 0) {
            val images = breedsApi.getImagesForBreed(breedId)
            database.imageDao().insertAll(images.map { it.asImageDbModel(breedId) })
        }
    }

    /** Returns Flow with ImageDbModel for given breedId. */
    fun observeImagesForBreed(breedId: String): Flow<List<ImageDbModel>> {
        return database.imageDao().observeAllForBreed(breedId)
    }

    /** Returns all images for given breedId. */
    suspend fun getImagesForBreed(breedId: String): List<ImageDbModel> {
        return database.imageDao().getAllForBreed(breedId)
    }
}