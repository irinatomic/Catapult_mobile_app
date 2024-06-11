package com.example.catapult.segments.breeds.details_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.breeds.BreedRepository
import com.example.catapult.data.mapper.asBreedUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository,
): ViewModel() {

    private val breedId = savedStateHandle.get<String>("breedId")!!

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = _state.getAndUpdate(reducer)

    init {
        // observeEvents() - no events from UI
        observeBreedDetails()

        testImages()
    }

    /** Observer breed details data from our local data and updates the state */
    private fun observeBreedDetails() {
        Log.d("IRINA", "observeBreedDetails: start")
        viewModelScope.launch {
            repository.observeBreedDetails(breedId = breedId)
                .filterNotNull()
                .collect {
                    Log.d("IRINA", "observeBreedDetails: $it")
                    setState { copy(breedUi = it.asBreedUiModel()) }
               }
        }
    }

    private fun testImages() {
        viewModelScope.launch(Dispatchers.IO) {
            // Observe the images for the breed from the database
            val initialImages = repository.observeImagesForBreed(breedId).first()
            Log.d("IRINA", "Initial images: $initialImages")

            // Fetch new images from the API
            repository.fetchImagesForBreed(breedId)

            // Observe the images again to see the updated values
            val updatedImages = repository.observeImagesForBreed(breedId).first()
            Log.d("IRINA", "Updated images: $updatedImages")
        }
    }

}