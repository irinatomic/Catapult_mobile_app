package com.example.catapult.segments.quiz.question_screen

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.quizQuestionScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val quizQuestionViewModel = viewModel<QuizQuestionViewModel>()
    val state by quizQuestionViewModel.state.collectAsState()

QuizQuestionScreen(
        state = state,
        onBack = { navController.popBackStack() },
        onSelectAnswer = { answer ->
            quizQuestionViewModel.setEvent(QuizQuestionUiEvent.AnswerSelected(answer))
        },
        onNextQuestion = {
            quizQuestionViewModel.setEvent(QuizQuestionUiEvent.NextQuestion)
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizQuestionScreen(
    state: QuizQuestionState,
    onBack: () -> Unit,
    onSelectAnswer: (String) -> Unit,
    onNextQuestion: () -> Unit
) {
    Scaffold (

        content = {
            if(state.creatingQuestions) {
                Text("Creating questions...")
            } else {
                Text("Questions created")
            }
        }
    )

}