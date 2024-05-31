package com.example.catapult.breeds.details_screen

import com.example.catapult.data.ui.BreedUiModel

data class BreedDetailsState(
    val breedId: String,
    val breedUi: BreedUiModel? = null,
    val error: DetailsError? = null
) {
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}
