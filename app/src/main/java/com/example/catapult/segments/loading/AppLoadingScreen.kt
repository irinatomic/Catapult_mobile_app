package com.example.catapult.segments.loading

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.appLoadingScreen (
    route: String,
) = composable(route = route) {

    AppLoadingScreen()
}

@Composable
fun AppLoadingScreen() {
    Text(text = "Loading...")
}
