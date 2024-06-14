import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.segments.breeds.gallery_screen.breedGalleryScreen
import com.example.catapult.segments.breeds.images_screen.breedImagesScreen
import com.example.catapult.segments.leaderboard.screen.leaderboardScreen
import com.example.catapult.segments.quiz.question_screen.quizQuestionScreen
import com.example.catapult.segments.quiz.start_screen.quizStartScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "breeds") {
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
    }
}