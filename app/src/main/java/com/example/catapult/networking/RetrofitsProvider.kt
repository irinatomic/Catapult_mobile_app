package com.example.catapult.networking

import com.example.catapult.networking.endpoints.BreedsApi
import com.example.catapult.networking.endpoints.LeaderboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitsProvider {

    @Provides
    @Singleton
    fun provideBreedsRetrofit(@Named("CatApiRetrofit") retrofit: Retrofit): BreedsApi = retrofit.create()

     @Provides
     @Singleton
     fun provideLeaderboardRetrofit(@Named("LeaderboardApiRetrofit") retrofit: Retrofit): LeaderboardApi = retrofit.create()
}