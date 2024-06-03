import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.navigation_drawer.DrawerMenu
import com.example.catapult.segments.breeds.images_screen.breedImagesScreen
import com.example.catapult.segments.quiz.question_screen.quizQuestionScreen
import com.example.catapult.segments.quiz.start_screen.quizStartScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(240.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column {
                    Text("Catapult",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Divider()
                    DrawerMenu(navController = navController) {
                        scope.launch { drawerState.close() }
                    }
                }
            }
        }
    ) {

        NavHost(navController = navController, startDestination = "breeds") {
            breedsListScreen(
                route = "breeds",
                navController = navController,
                drawerState = drawerState,
            )

            breedDetailsScreen(
                route = "breed/details/{breedId}",
                arguments = listOf(
                    navArgument("breedId") {
                        type = NavType.StringType
                    },
                ),
                navController = navController,
            )

            breedImagesScreen(
                route = "breed/images/{breedId}",
                arguments = listOf(
                    navArgument("breedId") {
                        type = NavType.StringType
                    },
                ),
                navController = navController,
            )

            quizStartScreen(
                route = "quiz/start",
                navController = navController,
                drawerState = drawerState,
            )

            quizQuestionScreen(
                route = "quiz",
                navController = navController,
            )
        }
    }
}