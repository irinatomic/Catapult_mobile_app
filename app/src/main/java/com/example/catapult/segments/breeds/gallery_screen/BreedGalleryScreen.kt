package com.example.catapult.segments.breeds.gallery_screen

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import com.example.catapult.R
import com.example.catapult.data.ui.ImageUiModel
import com.example.catapult.segments.breeds.gallery_screen.BreedGalleryContract.*

fun NavGraphBuilder.breedGalleryScreen(
    route: String,
    arguments: List<NamedNavArgument>,
    onBack: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) {navBackStackEntry ->

    val breedId = navBackStackEntry.arguments?.getString("breedId")
        ?: throw IllegalStateException("breedId required")
    val currentImage = navBackStackEntry.arguments?.getString("currentImage")
        ?: throw IllegalStateException("currentImage required")

    val breedGalleryViewModel = viewModel<BreedGalleryViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BreedGalleryViewModel(breedId = breedId, currentImage = currentImage) as T
            }
        }
    )

    val state = breedGalleryViewModel.state.collectAsState()

    BreedGalleryScreen(
        state = state.value,
        onBack = onBack,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedGalleryScreen(
    state: BreedGalleryState,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { state.images.size }
    )

    LaunchedEffect(state.images, state.currentIndex) {
        if (state.images.isNotEmpty()) {
            pagerState.scrollToPage(state.currentIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                })
        },

        content = { paddingValues ->
            if (state.images.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = { state.images[it].id }
                ) { pageIndex ->
                    val image = state.images[pageIndex]
                    ImagePreview(
                        modifier = Modifier,
                        image = image,
                    )
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No images.",
                )
            }
        },
    )
}

@Composable
fun ImagePreview(
    modifier: Modifier,
    image: ImageUiModel,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = image.url,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp),)
                }
            },
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}