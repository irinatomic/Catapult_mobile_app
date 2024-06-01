package com.example.catapult.navigation_drawer

sealed class DrawerItem(val route: String, val title: String) {
    object Discover : DrawerItem("breeds", "Discover")

    /*
    object Quiz : DrawerItem("quiz", "Quiz 1")
    object Leaderboard : DrawerItem("leaderboard", "Leaderboard")
    object Account : DrawerItem("account", "My Account")
    */

    companion object {
        val allItems = listOf(
            Discover
        )
    }
}