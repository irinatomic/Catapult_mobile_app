package com.example.catapult.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class UserDataSerializer: Serializer<UserData> {

    /** This is the user's account.
     * There can be only 1 account.
     * When the account is not created, the default value of empty Strings is used.
     * Default value cannot be null due to the limitations of Kotlin Serialization.
     */
    override val defaultValue: UserData = UserData()

    override suspend fun readFrom(input: InputStream): UserData {
        return withContext(Dispatchers.IO) {
            val inputString = input.bufferedReader().use { it.readText() }
            Json.decodeFromString(UserData.serializer(), inputString)
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val outputString = Json.encodeToString(t)
            output.write(outputString.toByteArray())
        }
    }
}