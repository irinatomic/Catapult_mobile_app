package com.example.catapult.segments.user.profile_screen

import com.example.catapult.data.database.entities.ResultDbModel
import com.example.catapult.data.datastore.UserData

interface ProfileContract {

    data class ProfileState(
        var fetchingData: Boolean = true,

        val userData: UserData = UserData(),
        val bestGlobalRank: Pair<Int, Double> = Pair(-1, -1.0),
        val totalGamesPlayed: Int = 0,
        val quizResults: List<ResultDbModel> = emptyList(),
    )
}