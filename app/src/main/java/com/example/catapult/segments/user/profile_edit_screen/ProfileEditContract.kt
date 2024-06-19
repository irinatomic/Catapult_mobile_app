package com.example.catapult.segments.user.profile_edit_screen

import com.example.catapult.data.datastore.UserData

interface ProfileEditContract {

    data class ProfileEditState(
        var fetchingData: Boolean = true,
        val userData: UserData = UserData(),
    )

    sealed class ProfileEditEvent {
        data class UpdateProfile(
            val firstName: String,
            val lastName: String,
            val nickname: String,
            val email: String
        ) : ProfileEditEvent() {

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