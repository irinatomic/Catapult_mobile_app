package com.example.catapult.segments.user.register_screen

import com.example.catapult.data.datastore.UserData

interface RegisterContract {

    sealed class RegisterEvent {
        data class Register(
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : RegisterEvent() {

            fun asUserData() : UserData {
                return UserData(
                    firstName = firstName,
                    lastName = lastName,
                    nickname = nickname,
                    email = email
                )
            }
        }

    }
}