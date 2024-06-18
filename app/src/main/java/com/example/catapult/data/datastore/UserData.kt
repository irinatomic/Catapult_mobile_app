package com.example.catapult.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserData (
    val firstName: String = "",
    val lastName: String = "",
    val nickname: String = "",
    val email: String = "",
)