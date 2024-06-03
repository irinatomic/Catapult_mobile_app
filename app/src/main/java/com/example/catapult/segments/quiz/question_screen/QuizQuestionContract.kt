package com.example.catapult.segments.quiz.question_screen

data class Question(
    val text: String,
    val breedImageUrl: String,
    val answers: List<String>,
    val correctAnswer: String
)

data class QuizQuestionState(
    val creatingQuestions: Boolean = true,

    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0
)

sealed class QuizQuestionUiEvent {
    data class AnswerSelected(val answer: String) : QuizQuestionUiEvent()
    object NextQuestion : QuizQuestionUiEvent()
}
