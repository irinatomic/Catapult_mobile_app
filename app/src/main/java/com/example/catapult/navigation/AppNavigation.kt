import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.navigation.NavigationViewModel
import com.example.catapult.segments.breeds.gallery_screen.breedGalleryScreen
import com.example.catapult.segments.breeds.images_screen.breedImagesScreen
import com.example.catapult.segments.leaderboard.screen.leaderboardScreen
import com.example.catapult.segments.loading.appLoadingScreen
import com.example.catapult.segments.quiz.question_screen.quizQuestionScreen
import com.example.catapult.segments.quiz.start_screen.quizStartScreen
import com.example.catapult.segments.user.profile_edit_screen.profileEditScreen
import com.example.catapult.segments.user.profile_screen.profileScreen
import com.example.catapult.segments.user.register_screen.registerScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val viewModel = hiltViewModel<NavigationViewModel>()
    val state by viewModel.state.collectAsState()

    val startDestination = when {
        state.isLoading -> "loading-screen"
        state.hasAccount -> "breeds"
        else -> "register"
    }

    NavHost(navController = navController, startDestination = startDestination) {

        appLoadingScreen(
            route = "loading-screen"
        )

        registerScreen(
            route = "register"
        )

        breedsListScreen(
            route = "breeds",
            navController = navController,
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
                    nullable = false
                    type = NavType.StringType
                },
            ),
            navController = navController,
            onBack = { navController.navigateUp() }
        )

        breedGalleryScreen(
            route = "breed/images/{breedId}?currentImage={currentImage}",
            arguments = listOf(
                navArgument(name = "breedId") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument(name = "currentImage") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onBack = { navController.navigateUp() }
        )

        quizStartScreen(
            route = "quiz/start",
            navController = navController,
        )

        quizQuestionScreen(
            route = "quiz",
            navController = navController,
        )

        leaderboardScreen(
            route = "leaderboard",
            navController = navController,
        )

        profileScreen(
            route = "profile",
            navController = navController,
        )

        profileEditScreen(
            route = "profile/edit",
            navController = navController,
        )
    }
}