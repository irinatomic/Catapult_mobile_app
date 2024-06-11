import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catapult.R
import com.example.catapult.data.ui.BreedUiModel
import com.example.catapult.navigation_drawer.AppDrawer
import com.example.catapult.navigation_drawer.DrawerMenu
import com.example.catapult.navigation_drawer.HamburgerMenu
import com.example.catapult.segments.breeds.list_screen.*
import kotlinx.coroutines.launch

fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val breedsListViewModel = hiltViewModel<BreedsListViewModel>()
    val state by breedsListViewModel.state.collectAsState()

    BreedsListScreen(
        state = state,
        eventPublisher = { breedsListViewModel.setEvent(it) },
        onItemClick = { breed ->
            navController.navigate("breed/details/${breed.id}")
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsListScreen(
    state: BreedsListState,
    eventPublisher: (BreedsListUIEvent) -> Unit,
    onItemClick: (BreedUiModel) -> Unit,
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
                scope = scope)
        }
    ) {
        Scaffold (

            // TOP BAR
            topBar = {
                if (!state.searchActive) {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = { HamburgerMenu(drawerState) },
                        actions = {
                            IconButton(onClick = { eventPublisher(BreedsListUIEvent.StartSearch) }) {
                                Icon(Icons.Filled.Search, contentDescription = stringResource(id = R.string.search))
                            }
                        }
                    )
                }
            },

            // CONTENT
            content = {
                if (state.searchActive) {
                    SearchMode(
                        state = state,
                        eventPublisher = eventPublisher,
                        onItemClick = onItemClick
                    )
                } else {
                    BreedsList(
                        items = state.breeds,
                        paddingValues = it,
                        onItemClick = onItemClick,
                    )

                    if (state.breeds.isEmpty()) {
                        when {
                            state.fetching -> FetchingData()
                            state.error is BreedsListState.ListError.ListUpdateFailed ->
                                ErrorData(errorMessage = state.error.cause?.message ?: stringResource(id = R.string.error_fetching_data))
                            else -> NoData()
                        }
                    }
                }
            },

            // NO BOTTOM BAR
        )
    }
}

@Composable
private fun SearchMode(
    state: BreedsListState,
    eventPublisher: (BreedsListUIEvent) -> Unit,
    onItemClick: (BreedUiModel) -> Unit,
) {

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(state.searchQuery, eventPublisher)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${stringResource(id = R.string.number_of_results)}: ${state.filteredBreeds.size}")
        BreedsList(
            items = state.filteredBreeds,
            paddingValues = PaddingValues(),
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    eventPublisher: (BreedsListUIEvent) -> Unit,
) {
    var searchText by remember { mutableStateOf(searchQuery) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { eventPublisher(BreedsListUIEvent.StopSearch) }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
        }

        TextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
                eventPublisher(BreedsListUIEvent.FilterSearch(newValue))
                Log.d("IRINA", "FilterSearch event sent: $newValue")
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(id = R.string.search)) }
        )

        IconButton(onClick = {
            searchText = ""
            eventPublisher(BreedsListUIEvent.DeleteSearch)
        }) {
            Icon(Icons.Filled.Close, contentDescription = stringResource(id = R.string.clear))
        }
    }
}

@Composable
private fun BreedsList (
    items: List<BreedUiModel>,
    paddingValues: PaddingValues,
    onItemClick: (BreedUiModel) -> Unit,
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        items(items, key = { item -> item.id }) { item ->
            BreedsPreviewCard(
                breed = item,
                onClick = { onItemClick(item) },
            )
        }
    }
}