package com.example.catapult.networking.api

import com.example.catapult.data.api.BreedApiModel
import com.example.catapult.data.api.ImageApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("images/search")
    suspend fun getImagesForBreed(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 20
    ): List<ImageApiModel>
}