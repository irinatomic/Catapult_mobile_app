import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapult.breeds.images_screen.breedImagesScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "breeds") {
        breedsListScreen(
            route = "breeds",
            navController = navController
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
    }
}