package com.example.catapult.breeds.list_screen

import com.example.catapult.data.ui.BreedUiModel

data class BreedsListState (
    val initialLoading: Boolean = true,
    val fetching: Boolean = false,
    val breeds: List<BreedUiModel> = emptyList(),
    val error: ListError? = null,

    val searchActive: Boolean = false,
    val searchQuery: String = "",
    val filteredBreeds: List<BreedUiModel> = emptyList(),
) {
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}