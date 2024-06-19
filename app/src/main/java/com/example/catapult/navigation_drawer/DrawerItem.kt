package com.example.catapult.navigation_drawer

import com.example.catapult.R

sealed class DrawerItem(
    val route: String,
    val title: String,
    val icon: Int
) {

    data object Discover : DrawerItem("breeds", "Discover",  R.drawable.ic_discover)
    data object StartQuiz : DrawerItem("quiz/start", "Start Quiz", R.drawable.ic_quiz)
    data object Leaderboard : DrawerItem("leaderboard", "Leaderboard", R.drawable.ic_leaderboard)
    data object Profile : DrawerItem("profile", "My Profile", R.drawable.ic_account)

    companion object {
        val allItems = listOf(
            Discover,
            StartQuiz,
            Leaderboard,
            Profile
        )
    }
}