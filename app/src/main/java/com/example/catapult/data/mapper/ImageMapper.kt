package com.example.catapult.data.mapper

import com.example.catapult.data.api.ImageApiModel
import com.example.catapult.data.database.entities.ImageDbModel
import com.example.catapult.data.ui.ImageUiModel

fun ImageApiModel.asImageDbModel(breedId: String): ImageDbModel {
    return ImageDbModel(
        id = id,
        breedId = breedId,
        url = url,
        width = width,
        height = height
    )
}

fun ImageDbModel.asImageUiModel(): ImageUiModel {
    return ImageUiModel(
        id = id,
        url = url,
        width = width,
        height = height
    )
}