package com.example.catapult.segments.quiz.question_screen

interface QuizQuestionContract {

    data class Question(
        val text: String,
        val breedId: String,
        var breedImageUrl: String? = "",
        val answers: List<String>,
        val correctAnswer: String
    )

    data class QuizQuestionState(
        val creatingQuestions: Boolean = true,
        val showCorrectAnswer: Boolean = false,
        val quizFinished: Boolean = false,

        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val correctAnswers: Int = 0,
        val timeLeft: Long = 0L,

        val score: Double = 0.0
    )

    sealed class QuizQuestionEvent {
        data class NextQuestion(val selected: String) : QuizQuestionEvent()
        data object TimeUp: QuizQuestionEvent()
        data class SubmitResult(val score: Double) : QuizQuestionEvent()
    }

}
