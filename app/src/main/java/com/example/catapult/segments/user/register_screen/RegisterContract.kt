package com.example.catapult.segments.user.register_screen

interface RegisterContract {

    sealed class RegisterEvent {
        data class Register(
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : RegisterEvent()
    }
}