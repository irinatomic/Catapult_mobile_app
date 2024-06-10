package com.example.catapult.segments.breeds.images_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.example.catapult.R
import androidx.compose.ui.Modifier
import com.example.catapult.data.ui.ImageUiModel

fun NavGraphBuilder.breedImagesScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
) = composable(route = route, arguments = arguments) { backStackEntry ->
    val breedId = backStackEntry.arguments?.getString("breedId")
        ?: throw IllegalArgumentException("id is required.")

    val breedImagesViewModel = viewModel<BreedImagesViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BreedImagesViewModel(breedId) as T
            }
        }
    )

    val onImageClick: (String) -> Unit = { imageId ->
        navController.navigate("breed/images/${breedId}?currentImage=$imageId")
    }

    val state = breedImagesViewModel.state.collectAsState()

    BreedImagesScreen(
        state = state.value,
        onImageClick = onImageClick,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedImagesScreen(
    state: BreedImagesState,
    onImageClick: (breedId: String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                title = { Text("Images for breed") }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = paddingValues,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(
                        items = state.images,
                        key = { index: Int, image: ImageUiModel -> image.id },
                    ) { index: Int, image: ImageUiModel ->
                        val aspectRatio = image.width.toFloat() / image.height.toFloat()
                        val imageHeightDp = cellSize / aspectRatio

                        Card(
                            modifier = Modifier
                                .width(cellSize)
                                .height(imageHeightDp)
                                .clickable { onImageClick(image.id) },
                            ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = image.url,
                                contentDescription = null,
                                loading = { CircularProgressIndicator() }
                            )
                        }
                    }

                }
            }
        }
    )
}


