package com.example.catapult.data.mapper

import com.example.catapult.data.api.LeaderboardApiModel
import com.example.catapult.data.database.entities.LBItemDbModel
import com.example.catapult.data.ui.LBItemUiModel

fun LeaderboardApiModel.asLBItemDbModel(
    allResults: List<LeaderboardApiModel>
): LBItemDbModel {

    val totalGamesPlayed = allResults.count { it.nickname == this.nickname }

    // id is automatically generated
    return LBItemDbModel(
        nickname = nickname,
        result = result,
        totalGamesPlayed = totalGamesPlayed,
        createdAt = createdAt
    )
}

fun LBItemDbModel.asLBItemUiModel(): LBItemUiModel {
    return LBItemUiModel(
        nickname = nickname,
        result = result,
        totalGamesPlayed = totalGamesPlayed,
    )
}