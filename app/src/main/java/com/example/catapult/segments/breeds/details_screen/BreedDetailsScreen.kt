import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import coil.compose.SubcomposeAsyncImage
import com.example.catapult.R
import com.example.catapult.segments.breeds.details_screen.BreedDetailsState
import com.example.catapult.segments.breeds.details_screen.BreedDetailsViewModel

fun NavGraphBuilder.breedDetailsScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
) = composable(route = route, arguments = arguments) { backStackEntry ->

    val breedId = backStackEntry.arguments?.getString("breedId") ?: ""

    val breedDetailsViewModel = hiltViewModel<BreedDetailsViewModel>(backStackEntry)
    val state = breedDetailsViewModel.state.collectAsState()

    BreedDetailsScreen(
        state = state.value,
        onBack = { navController.popBackStack() },
        onMoreImagesClick = {
            navController.navigate("breed/images/$breedId")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen (
    state: BreedDetailsState,
    onBack: () -> Unit,
    onMoreImagesClick: () -> Unit,
) {
    Surface {
        Column {
            // TOP BAR
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                title = { Text(stringResource(id = R.string.breed_details)) }
            )

            // CONTENT
            BreedDetails(
                state = state,
                paddingValues = PaddingValues(), // Adjust as necessary
                onMoreImagesClick = onMoreImagesClick
            )

            Log.d("IRINA", "BreedDetailsScreen: ${state.breedId}")

            if (state.breedUi == null) {
                when {
                    state.error is BreedDetailsState.DetailsError.DataUpdateFailed ->
                        ErrorData(errorMessage = state.error.cause?.message ?: stringResource(id = R.string.error_fetching_data))
                    else -> NoData()
                }
            }
        }
    }

}

@Composable
private fun BreedDetails(
    state: BreedDetailsState,
    paddingValues: PaddingValues,
    onMoreImagesClick: () -> Unit,
) {

    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        state.breedUi?.let { info ->
            BreedImage(imageUrl = info.imageUrl)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(id = R.string.breed_name)}: ${info.name}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )

                Button(
                    onClick = onMoreImagesClick,
                    modifier = Modifier.padding(8.dp)
                ) { Text(text = "More images") }
            }

            Text(
                text = info.description,
                modifier = Modifier.padding(8.dp)
            )

            Text (
                text = "${stringResource(id = R.string.breed_temperament)}: ${info.temperament.joinToString(", ")}",
                modifier = Modifier.padding(8.dp)
            )

            if(info.altNames == emptyList<String>()) {
                Text(
                    text = "${stringResource(id = R.string.breed_alt_names)}: ${info.altNames.joinToString(", ")}",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text (
                text = "${stringResource(id = R.string.breed_origin)}: ${info.origins.joinToString(", ")}",
                modifier = Modifier.padding(8.dp)
            )

            Text (
                text = "${stringResource(id = R.string.breed_life_span)}: ${info.lifeSpan} years",
                modifier = Modifier.padding(8.dp)
            )

            Text (
                text = "${stringResource(id = R.string.breed_rare_breed)}: ${if (info.rare == 1) "Yes" else "No"}",
                modifier = Modifier.padding(8.dp)
            )

            BreedCharacteristic(stringResource(id = R.string.breed_adaptability), info.adaptability)
            BreedCharacteristic(stringResource(id = R.string.breed_affection_level), info.affectionLevel)
            BreedCharacteristic(stringResource(id = R.string.breed_child_friendly), info.childFriendly)
            BreedCharacteristic(stringResource(id = R.string.breed_intelligence), info.intelligence)
            BreedCharacteristic(stringResource(id = R.string.breed_shedding_level), info.sheddingLevel)

            if (info.weight.isNotEmpty()) {
                BreedWeight(info.weight[0], info.weight[1])
            }

            WikipediaButton(wikipediaUrl = info.wikipediaUrl)
        }
    }
}

@Composable
private fun BreedImage(
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = null,
            loading = { CircularProgressIndicator() }
        )
    }
}

@Composable
private fun BreedCharacteristic(
    name: String,
    value: Int = 5
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = Modifier.padding(8.dp)) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (modifier = Modifier
                .weight(1f)
                .widthIn(max = screenWidth / 2)) {
                Text(text = name, modifier = Modifier.padding(end = 8.dp))
            }
            Column (modifier = Modifier
                .weight(1f)
                .widthIn(max = screenWidth / 2)) {
                StarRating(value = value)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun BreedWeight(
    imperial: String,
    metric: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {
            Text(
                text = "${stringResource(id = R.string.breed_weight)}:",
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$imperial ${stringResource(id = R.string.weight_imperial)}",
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "$metric ${stringResource(id = R.string.weight_metric)}",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun WikipediaButton(
    wikipediaUrl: String
){
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Button(
        onClick = {
            try {
                uriHandler.openUri(wikipediaUrl)
            } catch (e: Exception) {
                Toast.makeText(context, "No browser found on device", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) { Text(stringResource(id = R.string.wikipedia)) }
}
