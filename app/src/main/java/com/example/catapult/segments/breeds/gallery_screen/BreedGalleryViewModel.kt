package com.example.catapult.segments.breeds.gallery_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.mapper.asImageUiModel
import com.example.catapult.segments.breeds.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.catapult.segments.breeds.gallery_screen.BreedGalleryContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

@HiltViewModel
class BreedGalleryViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository
) : ViewModel() {

    private val breedId = savedStateHandle.get<String>("breedId")!!
    private val currentImage = savedStateHandle.get<String>("currentImage")!!

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