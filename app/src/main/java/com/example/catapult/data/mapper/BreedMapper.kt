package com.example.catapult.data.mapper

import com.example.catapult.data.api.BreedApiModel
import com.example.catapult.data.database.entities.BreedDbModel
import com.example.catapult.data.ui.BreedUiModel

fun BreedApiModel.asBreedDbModel(): BreedDbModel {
    return BreedDbModel(
        id = id,
        name = name?: "",
        altNames = altNames?: "",
        description = description?: "",
        temperament = temperament?: "",
        origin = origin?: "",
        lifeSpan = lifeSpan?: "",
        weightImperial = weight?.imperial ?: "",
        weightMetric = weight?.metric ?: "",

        // traits
        adaptability = adaptability ?: 0,
        affectionLevel = affectionLevel ?: 0,
        childFriendly = childFriendly ?: 0,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel ?: 0,
        strangerFriendly = strangerFriendly ?: 0,
        rare = rare ?: -1,

        wikipediaUrl = wikipediaUrl,
        imageUrl = image?.imageUrl ?: ""
    )
}

fun BreedDbModel.asBreedUiModel(): BreedUiModel {
    return BreedUiModel(
        id = id,
        name = name,
        altNames = altNames.split(", "),
        description = description,
        temperament = temperament.split(", "),
        origins = origin.split(", "),
        lifeSpan = lifeSpan,
        weight = listOf(weightImperial, weightMetric),
        adaptability = adaptability,
        affectionLevel = affectionLevel,
        childFriendly = childFriendly,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel,
        rare = rare,
        wikipediaUrl = wikipediaUrl,
        imageUrl = imageUrl
    )
}