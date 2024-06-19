package com.example.catapult.segments.user.profile_edit_screen

import com.example.catapult.segments.user.profile_edit_screen.ProfileEditContract.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){

    private val _state = MutableStateFlow(ProfileEditState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ProfileEditState.() -> ProfileEditState) = _state.update(reducer)

    private val events = MutableSharedFlow<ProfileEditEvent>()

    fun setEvent(event: ProfileEditEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
        fetchProfileInfo()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when(it) {
                    is ProfileEditEvent.UpdateProfile -> {
                        repository.updateUser(it.asUserData())
                        Log.d("IRINA", "updateProfileInfo event")
                    }
                }
            }
        }
    }

    private fun fetchProfileInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val userData = repository.getUserData()
                setState {
                    copy(
                        userData = userData,
                        fetchingData = false
                    )
                }
            }
        }
    }
}