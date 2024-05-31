package com.example.catapult.breeds.details_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.breeds.repository.BreedInfoRepository
import com.example.catapult.data.mapper.asBreedUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class BreedDetailsViewModel(
    private val breedId: String,
    private val repository: BreedInfoRepository = BreedInfoRepository,
): ViewModel() {

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()

    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) = _state.getAndUpdate(reducer)

    init {
        // observeEvents() - no events from UI
        observeBreedDetails()
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

}