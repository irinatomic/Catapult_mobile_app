package com.example.catapult.segments.breeds.gallery_screen

import com.example.catapult.data.ui.ImageUiModel

interface BreedGalleryContract {

    data class BreedGalleryState(
        val loading: Boolean = false,
        val images: List<ImageUiModel> = emptyList(),
        val currentIndex: Int = 0,
    )
}