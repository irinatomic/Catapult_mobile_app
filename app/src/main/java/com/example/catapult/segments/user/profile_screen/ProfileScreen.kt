package com.example.catapult.segments.user.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.R
import com.example.catapult.segments.user.profile_screen.ProfileContract.*
import com.example.catapult.data.database.entities.ResultDbModel
import com.example.catapult.data.datastore.UserData
import com.example.catapult.navigation_drawer.*

fun NavGraphBuilder.profileScreen(
    route: String,
    navController: NavController
) = composable(route = route) {

    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val state by profileViewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onEditClick = { navController.navigate("profile/edit") },
        navController = navController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onEditClick: () -> Unit,
    navController: NavController,
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
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                // TOP BAR
                TopAppBar(
                    title = { Text(stringResource(id = R.string.app_name)) },
                    navigationIcon = { HamburgerMenu(drawerState) }
                )

                // CONTENT
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 42.dp, vertical = 16.dp)
                ) {
                    if (state.fetchingData) {
                        Text("Loading...")
                    } else {
                        Column {
                            UserInfo(user = state.userData)
                            BestGlobalRank(bestGlobalRank = state.bestGlobalRank)
                            QuizHistorySegment(quizResults = state.quizResults)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = onEditClick,
                                    modifier = Modifier.padding(top = 16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) { Text(text = "Edit Profile") }
                            }
                        }
                    }
                }

                // NO BOTTOM BAR
            }
        }
    }
}

@Composable
fun UserInfo(
    user: UserData
) {
    Text(
        "Profile Info",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        InformationRow(
            title = "First Name: ",
            value = user.firstName,
            addUnderline = true
        )
        InformationRow(
            title = "Last Name: ",
            value = user.lastName,
            addUnderline = true
        )
        InformationRow(
            title = "Email: ",
            value = user.email,
            addUnderline = true
        )
        InformationRow(
            title = "Nickname: ",
            value = user.nickname,
            addUnderline = true
        )
    }
}

@Composable
fun BestGlobalRank(
    bestGlobalRank: Pair<Int, Double>
) {
    Text(
        text = "Best Global Rank",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )
    Column(modifier = Modifier.padding(bottom = 22.dp)) {
        InformationRow(
            title = "Rank: ",
            value = bestGlobalRank.first.toString(),
            addUnderline = false
        )
        InformationRow(
            title = "Score: ",
            value = bestGlobalRank.second.toString(),
            addUnderline = false
        )
    }
}

@Composable
fun QuizHistorySegment(
    quizResults: List<ResultDbModel>
) {
    Text(
        text = "Quiz History",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )

    InformationRow(
        title = "Games played",
        value = quizResults.size.toString(),
        addUnderline = false
    )

    Spacer(modifier = Modifier.height(16.dp))
    QuizHistoryTable(quizResults = quizResults)
}


@Composable
fun QuizHistoryTable(
    quizResults: List<ResultDbModel>
) {
    val borderWidth = 1.dp
    val headerBackgroundColor = Color(0xFFA2A2BD)

    QuizHistoryTableContent(
        quizResults = quizResults,
        borderWidth = borderWidth,
        headerBackgroundColor = headerBackgroundColor
    )
}

@Composable
fun QuizHistoryTableContent(
    quizResults: List<ResultDbModel>,
    borderWidth: Dp,
    headerBackgroundColor: Color
) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackgroundColor)
                    .border(borderWidth, Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val modifier = Modifier.weight(1f)

                QuizHistoryTableField(text = "Result", modifier = modifier)
                QuizHistoryTableField(text = "Created At", modifier = modifier)
                QuizHistoryTableField(text = "Published", modifier = modifier)
            }
        }

        items(quizResults) { result ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(borderWidth, Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val modifier = Modifier.weight(1f)

                QuizHistoryTableField(text = result.result.toString(), modifier = modifier)
                QuizHistoryTableField(text = formatTimestamp(result.createdAt), modifier = modifier)
                QuizHistoryTableField(text = if (result.published) "Yes" else "No", modifier = modifier)
            }
        }
    }
}


@Composable
fun QuizHistoryTableField(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun InformationRow(
    title: String,
    value: String,
    addUnderline: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Box(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterStart).padding(bottom = 1.dp)
            )
            if (addUnderline) {
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp)
    return sdf.format(date)
}