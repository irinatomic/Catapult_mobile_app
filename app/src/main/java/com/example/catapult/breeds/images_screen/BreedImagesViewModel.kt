package com.example.catapult.breeds.images_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.list_screen.BreedsListState
import com.example.catapult.breeds.repository.BreedRepository
import com.example.catapult.data.mapper.asImageUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class BreedImagesViewModel(
    private val breedId: String,
    private val repository: BreedRepository = BreedRepository,
): ViewModel() {

    private val _state = MutableStateFlow(BreedImagesState(breedId = breedId))
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedImagesState.() -> BreedImagesState) = _state.getAndUpdate(reducer)

    init {
        observeImages()
        fetchImages()
    }

    /**
     * Observes images for the breed from the database and updates the state.
     */
    private fun observeImages() {
        viewModelScope.launch {
            repository.observeImagesForBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(images = it.map { it.asImageUiModel() }) }
                }
        }
    }

    /**
     * Fetches images for the breed from api endpoint and saves them to the database.
     * Only if we don't have any images for the breed in the database.
     */
    private fun fetchImages() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchImagesForBreed(breedId)
                }
            } catch (error: IOException) {
                setState { copy(error = BreedsListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
        Log.i(" IRINA", "Images table updated.")
    }

}