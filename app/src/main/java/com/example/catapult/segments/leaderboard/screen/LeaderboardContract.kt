package com.example.catapult.segments.leaderboard.screen

import com.example.catapult.data.ui.LBItemUiModel

interface LeaderboardContract {

    data class LeaderboardState(
        val leaderboardItems: List<LBItemUiModel> = emptyList(),
        val fetching: Boolean = false,
        val error: ListError? = null,
    ) {
        sealed class ListError {
            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
        }
    }
}