package com.example.catapult.segments.leaderboard.screen

import com.example.catapult.segments.leaderboard.screen.LeaderboardContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.mapper.asLBItemUiModel
import com.example.catapult.segments.leaderboard.LeaderboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
): ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.update(reducer)

    init {
        observeLeaderboard()
        fetchLeaderboard()
    }

    private fun observeLeaderboard() {
        viewModelScope.launch {
            repository.observeLeaderboard().collect {
                setState { copy(
                    leaderboardItems = it.map { it.asLBItemUiModel() }
                ) }
            }
        }
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchLeaderboard()
                }
            }  catch (error: IOException) {
                setState { copy(error = LeaderboardState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
            setState { copy(fetching = false) }
        }
    }
}