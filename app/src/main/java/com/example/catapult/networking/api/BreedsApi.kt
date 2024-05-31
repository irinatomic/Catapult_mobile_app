package com.example.catapult.networking.api

import com.example.catapult.data.api.BreedApiModel
import retrofit2.http.GET

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>
}