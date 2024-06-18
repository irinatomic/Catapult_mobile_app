package com.example.catapult.segments.quiz.question_screen

import com.example.catapult.segments.quiz.question_screen.QuizQuestionContract.*
import com.example.catapult.segments.quiz.QuizRepository
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.segments.leaderboard.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizQuestionViewModel @Inject constructor (
    private val quizRepository: QuizRepository,
    private val lbRepository: LeaderboardRepository,
): ViewModel() {

    private val _state = MutableStateFlow(QuizQuestionState())
    val state = _state.asStateFlow()

    private fun setState(reducer: QuizQuestionState.() -> QuizQuestionState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow <QuizQuestionEvent>()

    fun setEvent(event: QuizQuestionEvent) {
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
            setEvent(QuizQuestionEvent.TimeUp)
        }
    }

    init {
        observeEvents()
        createQuestions()
    }

    /** Observe events sent from UI to this View Model */
    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when(it) {
                    is QuizQuestionEvent.NextQuestion -> {
                        // fetch image for the next next question
                        val currQuestIndex = state.value.currentQuestionIndex
                        if(currQuestIndex + 2 < state.value.questions.size) {
                            viewModelScope.launch(Dispatchers.IO) {
                                quizRepository.fetchImagesForBreed(state.value.questions[currQuestIndex + 2])
                            }
                        }

                        setState { copy(showCorrectAnswer = true)}
                        delay(1000)
                        setState { copy(showCorrectAnswer = false)}

                        val currentQuestion = state.value.questions[currQuestIndex]
                        val correctAnswer = currentQuestion.correctAnswer
                        val selectedAnswer = it.selected

                        if (correctAnswer == selectedAnswer)
                            setState { copy(correctAnswers = correctAnswers + 1) }

                        if (state.value.currentQuestionIndex < state.value.questions.size - 1)
                            setState { copy(currentQuestionIndex = currentQuestionIndex + 1) }
                        else
                            endQuiz()
                    }

                    is QuizQuestionEvent.TimeUp -> { endQuiz() }

                    is QuizQuestionEvent.SubmitResult -> { lbRepository.submitQuizResult(result = it.score) }
                }
            }
        }
    }

    /** Create questions for the quiz so the user can start the quiz.
     *  Fetch images for the first two questions.
     */
    private fun createQuestions() {
        viewModelScope.launch {
            Log.d("IRINA", "Creating questions...")

            withContext(Dispatchers.IO) {
                setState { copy(creatingQuestions = true) }
                val questions = quizRepository.generateQuestions()
                quizRepository.fetchImagesForBreed(questions[0])
                quizRepository.fetchImagesForBreed(questions[1])
                setState { copy(questions = questions, creatingQuestions = false) }

                Log.d("IRINA", "Questions created $questions")
            }

            timer.start()
        }
    }

    private suspend fun endQuiz() {
        timer.cancel()
        setState { copy(quizFinished = true) }

        // Calculate score
        val score = calculateScore()
        setState { copy(score = score) }

        // Save to database
        quizRepository.submitResultToDatabase(score)
    }

    private fun calculateScore(): Double {
        val NCA = state.value.correctAnswers                    // Number of Correct Answers
        val TD = 300                                            // Time duration
        val TL = state.value.timeLeft.toInt()                   // Time left

        return (NCA * 2.5 * (1 + (TL + 120) / TD)).coerceAtMost(100.0)
    }
}