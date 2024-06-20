package com.example.catapult.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// SingletonComponent is a Dagger component that is tied to the Application lifecycle
// and will be created only once during the Application lifecycle.

@Module
@InstallIn(SingletonComponent::class)
object UserStoreModule {

    @Provides           // tells Dagger how to provide instances of a type
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): DataStore<UserData> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("auth.txt") },
            serializer = UserDataSerializer()
        )
}