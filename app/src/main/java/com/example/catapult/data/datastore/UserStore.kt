package com.example.catapult.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    private val persistence: DataStore<UserData>,
){

    private val scope = CoroutineScope(Dispatchers.IO)

    val userData: Flow<UserData> = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),            // starts when the first subscriber appears
            initialValue = UserData()
        )

    suspend fun isUserRegistered(): Boolean {
        return try {
            val user = persistence.data.first()
            user.email.isNotEmpty() &&
            user.firstName.isNotEmpty() &&
            user.lastName.isNotEmpty() &&
            user.nickname.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserData(): UserData {
        return try {
            persistence.data.first()
        } catch (e: Exception) {
            UserData()
        }
    }

    suspend fun setData(data: UserData) {
        try {
            persistence.updateData { data }
        } catch (e: Exception) {
            Log.d("IRINA", "Error setting data: $e")
        }
    }

    suspend fun updateData(update: UserData.() -> UserData) {
        try {
            persistence.updateData { currentData -> update(currentData) }
        } catch (e: Exception) {
            Log.d("IRINA", "Error updating data: $e")
        }
    }

    suspend fun deleteUser() {
        try {
            persistence.updateData { UserData() }        // update data with default values
        } catch (e: Exception) {
            Log.d("IRINA", "Error deleting data: $e")
        }
    }
}