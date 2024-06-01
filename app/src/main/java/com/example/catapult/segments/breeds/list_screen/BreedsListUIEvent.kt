package com.example.catapult.segments.breeds.list_screen

sealed class BreedsListUIEvent {
    data object StartSearch : BreedsListUIEvent()
    data object StopSearch : BreedsListUIEvent()
    data object DeleteSearch : BreedsListUIEvent()
    data class FilterSearch(val query: String): BreedsListUIEvent()
}