package com.example.catapult.segments.quiz.question_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.quiz.QuizRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class QuizQuestionViewModel(
    private val repository: QuizRepository = QuizRepository
): ViewModel() {

    private val _state = MutableStateFlow(QuizQuestionState())
    val state = _state.asStateFlow()

    private fun setState(reducer: QuizQuestionState.() -> QuizQuestionState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow <QuizQuestionUiEvent>()
    fun setEvent(event: QuizQuestionUiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }
    init {
        observeEvents()
        // observeQuestions - not needed since the questions are created in the repository
        createQuestions()
    }

    /** Observe events sent from UI to this View Model */
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when(it) {
                    is QuizQuestionUiEvent.AnswerSelected -> TODO()
                    QuizQuestionUiEvent.NextQuestion -> TODO()
                }
            }
        }
    }

    private fun createQuestions() {
        viewModelScope.launch {
            Log.d("IRINA", "Creating questions...")

            setState { copy(creatingQuestions = true) }
            val questions = repository.generateQuestions()
            setState { copy(questions = questions, creatingQuestions = false) }

            Log.d("IRINA", "Questions created $questions")
        }
    }
}