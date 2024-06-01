package com.example.catapult.segments.breeds.list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.breeds.BreedRepository
import com.example.catapult.data.mapper.asBreedUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class BreedsListViewModel(
    private val repository: BreedRepository = BreedRepository
): ViewModel() {

     private val _state = MutableStateFlow(BreedsListState())
     val state = _state.asStateFlow()
     private fun setState(reducer: BreedsListState.() -> BreedsListState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<BreedsListUIEvent>()
    fun setEvent(event: BreedsListUIEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
        observeBreeds()
        fetchBreeds()
    }

    /** Observe events sent from UI to this View Model */
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is BreedsListUIEvent.StartSearch -> setState { copy(searchActive = true, filteredBreeds = state.value.breeds) }
                    is BreedsListUIEvent.StopSearch -> setState { copy(searchActive = false, filteredBreeds = emptyList()) }
                    is BreedsListUIEvent.FilterSearch -> {
                        Log.d("IRINA", "FilterSearch event received")
                        setState  { copy(filteredBreeds = emptyList()) }
                        val filteredBreeds = state.value.breeds.filter { breed -> breed.name.contains(it.query, ignoreCase = true) }
                        setState { copy(filteredBreeds = filteredBreeds) }
                        setState { copy(searchQuery = it.query) }
                    }
                    is BreedsListUIEvent.DeleteSearch -> {
                        setState{ copy(searchQuery = "") }
                        setState{ copy(filteredBreeds = state.value.breeds) }
                    } else -> {
                        Log.d("IRINA", "Event not recognized")
                    }
                }
            }
        }
    }

    /**
     * This will observe all breeds and update state whenever
     * underlying data changes. We are using viewModelScope which
     * will cancel the subscription when view model dies.
     * SET BOTH breeds and filteredBreeds to the same list
     */
    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(initialLoading = true) }
            repository.observeBreeds()
                .distinctUntilChanged()
                .collect {
                    setState {
                        copy(
                            initialLoading = false,
                            breeds = it.map { it.asBreedUiModel() },
                            filteredBreeds = it.map { it.asBreedUiModel() }
                        )
                    }
                }
        }
    }

    /**
     * Fetches breeds from api endpoint and
     * replaces existing breeds with "downloaded" breeds.
     */
    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreeds()
                }
            } catch (error: IOException) {
                setState { copy(error = BreedsListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
        Log.i(" IRINA", "Breeds table updated.")
    }

}