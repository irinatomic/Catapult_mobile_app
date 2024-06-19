package com.example.catapult.segments.quiz.start_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.R
import com.example.catapult.navigation_drawer.HamburgerMenu
import androidx.compose.ui.graphics.Color
import com.example.catapult.navigation_drawer.AppDrawer

fun NavGraphBuilder.quizStartScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    QuizStartScreen(
        onStartQuizClick = { navController.navigate("quiz") },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStartScreen (
    onStartQuizClick: () -> Unit,
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        Surface {
            Column {
                // TOP BAR
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_name)) },
                    navigationIcon = { HamburgerMenu(drawerState) }
                )

                // CONTENT
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onStartQuizClick,
                        modifier = Modifier.padding(16.dp), // Add padding here
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) { Text(text = "Start Quiz", color = Color.White) }
                }

                // NO BOTTOM BAR
            }
        }
    }
}