package com.example.catapult.segments.leaderboard.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.catapult.segments.leaderboard.screen.LeaderboardContract.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catapult.R
import com.example.catapult.data.ui.LBItemUiModel
import com.example.catapult.navigation_drawer.*

fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController
) = composable(route = route) {

    val viewModel = hiltViewModel<LeaderboardViewModel>()
    val state by viewModel.state.collectAsState()

    LeaderboardScreen(
        state = state,
        navController = navController,
        onBack = { /* Do nothing here to disable the back action */ },
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    state: LeaderboardState,
    navController: NavController,
    onBack: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                navController = navController,
                drawerState = drawerState,
                scope = scope)
        }
    ) {
        Scaffold(
            // TOP BAR
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_name)) },
                    navigationIcon = { IconButton(onClick = onBack) { HamburgerMenu(drawerState) } },
                )
            },

            // CONTENT
            content = {paddingValues ->

                val startPadding = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp
                val topPadding = paddingValues.calculateTopPadding()
                val endPadding = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp
                val bottomPadding = paddingValues.calculateBottomPadding()

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = startPadding,
                        top = topPadding,
                        end = endPadding,
                        bottom = bottomPadding
                    )
                )
                {
                    when {
                        state.fetching -> {
                            // Display a loading indicator while fetching data
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                        state.error != null -> {
                            when (val error = state.error) {
                                is LeaderboardState.ListError.ListUpdateFailed -> {
                                    Text(text = "Error: ${error.cause?.message}", Modifier.align(Alignment.Center))
                                }
                                else -> { Log.d("IRINA", error.toString()) }
                            }
                        }
                        else -> {
                            Leaderboard(leaderboardItems = state.leaderboardItems)
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun Leaderboard(
    leaderboardItems: List<LBItemUiModel>
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LeaderboardHeader()
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            itemsIndexed(leaderboardItems) { index, item ->
                LeaderboardCard(orderNumber = index, item = item)
            }
        }
    }
}

@Composable
fun LeaderboardHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "No.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(50.dp))
        Text(text = "Nickname", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(130.dp))
        Text(text = "Result", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(70.dp))
        Text(text = "Games No.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(70.dp))
    }
}

@Composable
fun LeaderboardCard(
    orderNumber: Int,
    item: LBItemUiModel
) {
    val cardColor = when (orderNumber) {
        0 -> Color(0xFFCEB462)          // Gold
        1 -> Color(0xFF9B9797)          // Silver
        2 -> Color(0xFFCE9964)          // Bronze
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),                                 // rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(cardColor.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val textStyle = MaterialTheme.typography.bodyMedium
            Text(text = (orderNumber + 1).toString(), style = textStyle, modifier = Modifier.padding(start = 12.dp).width(50.dp))
            Text(text = item.nickname, style = textStyle, modifier = Modifier.width(130.dp))
            Text(text = String.format("%.2f", item.result), style = textStyle, modifier = Modifier.width(70.dp))
            Text(text = item.totalGamesPlayed.toString(), style = textStyle, modifier = Modifier.padding(end = 12.dp).width(70.dp))
        }
    }
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    val mockNavController = rememberNavController()

    LeaderboardScreen(
        state = LeaderboardState(
            fetching = false,
            error = null,
            leaderboardItems = listOf(
                LBItemUiModel(
                    ranking = 1,
                    nickname = "nick",
                    result = 25.0,
                    totalGamesPlayed = 3,
                ),
                LBItemUiModel(
                    ranking = 2,
                    nickname = "mike",
                    result = 20.0,
                    totalGamesPlayed = 2,
                ),
                LBItemUiModel(
                    ranking = 3,
                    nickname = "nick",
                    result = 19.0,
                    totalGamesPlayed = 3,
                ),
                LBItemUiModel(
                    ranking = 3,
                    nickname = "nick",
                    result = 17.0,
                    totalGamesPlayed = 3,
                ),
                LBItemUiModel(
                    ranking = 5,
                    nickname = "mike",
                    result = 10.0,
                    totalGamesPlayed = 2,
                ),
            )
        ),
        navController = mockNavController,
        onBack = {}
    )
}

