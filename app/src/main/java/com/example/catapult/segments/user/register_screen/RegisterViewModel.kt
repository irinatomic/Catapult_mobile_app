package com.example.catapult.segments.user.register_screen

import android.util.Log
import com.example.catapult.data.datastore.UserStore
import com.example.catapult.segments.user.register_screen.RegisterContract.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.datastore.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val store: UserStore
): ViewModel() {

    private val events = MutableSharedFlow<RegisterEvent>()

    fun setEvent(event: RegisterEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
    }

    /** Observe events sent from UI to this View Model */
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when(it) {
                    is RegisterEvent.Register -> { registerUser(it) }
                }
            }
        }
    }

    private suspend fun registerUser(event: RegisterEvent.Register) {
        val userData = UserData(
            firstName = event.firstName,
            lastName = event.lastName,
            nickname = event.nickname,
            email = event.email
        )
        Log.d("IRINA", "Registering user: $userData")
        store.setData(userData)

        // TODO: navigation to /breeds screen
    }
}