package com.example.catapult.navigation_drawer

sealed class DrawerItem(val route: String, val title: String) {

    data object Discover : DrawerItem("breeds", "Discover")
    data object StartQuiz : DrawerItem("quiz/start", "Start Quiz")
    data object Leaderboard : DrawerItem("leaderboard", "Leaderboard")

   // object Account : DrawerItem("account", "My Account")

    companion object {
        val allItems = listOf(
            Discover,
            StartQuiz,
            Leaderboard
        )
    }
}