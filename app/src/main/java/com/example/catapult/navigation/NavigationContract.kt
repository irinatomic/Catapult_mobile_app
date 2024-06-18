package com.example.catapult.navigation

interface NavigationContract {

    data class NavigationState(
        val hasAccount: Boolean = false,
    )
}