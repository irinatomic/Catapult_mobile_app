package com.example.catapult.segments.quiz.question_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.catapult.R
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

fun NavGraphBuilder.quizQuestionScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val quizQuestionViewModel = viewModel<QuizQuestionViewModel>()
    val state by quizQuestionViewModel.state.collectAsState()

QuizQuestionScreen(
        state = state,
        onBack = { navController.popBackStack() },
        onNextQuestion = { answer ->
            quizQuestionViewModel.setEvent(QuizQuestionUiEvent.NextQuestion(answer))
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizQuestionScreen(
    state: QuizQuestionState,
    onBack: () -> Unit,
    onNextQuestion: (String) -> Unit
) {

    Scaffold (
        content = {
            if(state.creatingQuestions)
                CreatingQuestions()
            else {
                ShowQuestion(
                    question = state.questions[state.currentQuestionIndex],
                    showCorrectAnswer = state.showCorrectAnswer,
                    onNextQuestion = onNextQuestion
                )
            }
        }
    )
}

@Composable
fun ShowQuestion(
    question: Question,
    showCorrectAnswer: Boolean,
    onNextQuestion: (String) -> Unit
) {
    var answer by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1F))
        Text(text = question.text, modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally))

        SubcomposeAsyncImage(
            model = question.breedImageUrl,
            loading = { Text("Loading...") },
            error = { Text("Image loading failed") },
            contentDescription = "Breed Image",
            modifier = Modifier.size(250.dp).align(Alignment.CenterHorizontally)
        )

        ShowOfferedAnswers(
            question = question,
            selectedAnswer = answer,
            onAnswerSelected = { selected -> answer = selected },
            showCorrectAnswer = showCorrectAnswer
        )

        Button(
            onClick = { onNextQuestion(answer) },
            enabled = answer.isNotEmpty(),
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
        ) { Text("Next Question") }
    }
}

@Composable
fun ShowOfferedAnswers(
    question: Question,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    showCorrectAnswer: Boolean              // when true, the correct answer is green and the wrong answers are red
) {
    val correctAnswer = question.correctAnswer

    question.answers.forEach { ans ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 85.dp)
        ) {
            RadioButton(
                selected = (ans == selectedAnswer),
                onClick = { onAnswerSelected(ans) },
            )
            Text(
                text = ans,
                modifier = Modifier.padding(start = 2.dp),
                color = if(showCorrectAnswer) {
                    if (ans != correctAnswer) MaterialTheme.colors.error
                    else Color.Green
                } else MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CreatingQuestions() {
    val randomTexts = listOf(
        "Hang in there, we're conjuring up some purr-ific questions!",
        "Stay whiskered, we're crafting some fur-nomenal queries!",
        "Hold onto your tails, we're tailoring some claw-some questions!",
        "Paws for a moment, we're brewing up some fur-tastic questions!",
        "Don't fur-get to purr-severe, we're fetching some meow-velous questions!"
    )

    val randomText = randomTexts.random()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.loading_image),
                contentDescription = "Loading image",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = randomText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun QuizQuestionScreenPreview() {
    QuizQuestionScreen(
        state = QuizQuestionState(
            creatingQuestions = false,
            questions = listOf(
                Question(
                    text = "Which temperament belongs to this breed?",
                    breedImageUrl = "https://cdn2.thecatapi.com/images/IOqJ6RK7L.jpg",
                    answers = listOf("affectionate", "loyal", "sensitive", "independent"),
                    correctAnswer = "loyal"
                )
            ),
            currentQuestionIndex = 0
        ),
        onBack = {},
        onNextQuestion = {}
    )
}