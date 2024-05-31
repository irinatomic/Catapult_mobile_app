package com.example.catapult.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["nickname"], unique = true)])
data class UserDbModel(

    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val nickname: String,
    val email: String,
    val avatarUrl: String,
) {
    init {
        require(firstName.isNotBlank()) { "First name must not be blank" }
        require(lastName.isNotBlank()) { "Last name must not be blank" }
        require(nickname.isNotBlank()) { "Nickname must not be blank" }
        require(email.isNotBlank()) { "Email must not be blank" }

        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
        require(email.matches(emailRegex)) { "Invalid email format" }
    }

    val fullName: String
        get() = "$firstName $lastName"
}
