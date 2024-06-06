package com.example.catapult.segments.quiz.question_screen

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.quiz.QuizRepository
import kotlinx.coroutines.delay
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

    // Timer
    private val timer = object: CountDownTimer(5 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            setState { copy(timeLeft = millisUntilFinished / 1000) }
        }
        override fun onFinish() {
            setEvent(QuizQuestionUiEvent.TimeUp)
        }
    }

    init {
        observeEvents()
        // observeQuestions not needed since the questions are created in the repository
        createQuestions()
        timer.start()
    }

    /** Observe events sent from UI to this View Model */
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when(it) {
                    is QuizQuestionUiEvent.NextQuestion -> {
                        setState { copy(showCorrectAnswer = true)}
                        delay(1000)
                        setState { copy(showCorrectAnswer = false)}

                        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
                        val correctAnswer = currentQuestion.correctAnswer
                        val selectedAnswer = it.selected

                        if (correctAnswer == selectedAnswer)
                            setState { copy(correctAnswers = correctAnswers + 1) }

                        if (state.value.currentQuestionIndex < state.value.questions.size - 1)
                            setState { copy(currentQuestionIndex = currentQuestionIndex + 1) }
                        else
                            endQuiz()
                    }

                    is QuizQuestionUiEvent.TimeUp -> { endQuiz() }
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

    private fun endQuiz() {
        timer.cancel()
        setState { copy(quizFinished = true) }

        val NCA = state.value.correctAnswers                    // Number of Correct Answers
        val TD = 300                                            // Time duration
        val TL = state.value.timeLeft.toInt()                   // Time left

        // score = NCA * 2.5 * (1 + (TL + 120) / TD)
        val score = (NCA * 2.5 * (1 + (TL + 120) / TD)).coerceAtMost(100.0)
        setState { copy(score = score.toInt()) }
    }
}