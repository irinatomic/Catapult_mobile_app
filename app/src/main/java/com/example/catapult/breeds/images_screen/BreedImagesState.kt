package com.example.catapult.breeds.images_screen

import com.example.catapult.breeds.list_screen.BreedsListState
import com.example.catapult.data.ui.ImageUiModel

data class BreedImagesState (
    val breedId: String,
    val fetching: Boolean = false,
    val error: BreedsListState.ListError? = null,

    val images: List<ImageUiModel> = emptyList()
) {
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}