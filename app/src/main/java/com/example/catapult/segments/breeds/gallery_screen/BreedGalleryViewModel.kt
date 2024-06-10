package com.example.catapult.segments.breeds.gallery_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.mapper.asImageUiModel
import com.example.catapult.segments.breeds.BreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.catapult.segments.breeds.gallery_screen.BreedGalleryContract.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate

class BreedGalleryViewModel (
    private val breedId: String,
    private val currentImage: String,
    private val repository: BreedRepository = BreedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BreedGalleryState())
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedGalleryState.() -> BreedGalleryState) = _state.getAndUpdate(reducer)

    init {
        observeImages()
    }

    private fun observeImages() {
        viewModelScope.launch {
            setState { copy(loading = true) }

            repository.observeImagesForBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(
                        images = it.map { it.asImageUiModel() },
                        currentIndex = images.indexOfFirst { it.id == currentImage }
                    ) }
                }

            setState { copy(loading = false) }
        }
    }
}