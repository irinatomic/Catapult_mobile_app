package com.example.catapult.networking.url_providers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiUrlsModule {

    @CatApiUrl
    @Provides
    fun provideCatApiUrl(): String {
        return "https://api.thecatapi.com/v1/"
    }

    @LeaderboardApiUrl
    @Provides
    fun provideLeaderboardApiUrl(): String {
        return "https://rma.finlab.rs/"
    }
}