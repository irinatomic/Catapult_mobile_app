package com.example.catapult.segments.quiz.question_screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.catapult.R
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.catapult.segments.quiz.question_screen.QuizQuestionContract.*
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.Icon
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage

fun NavGraphBuilder.quizQuestionScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val quizQuestionViewModel = hiltViewModel<QuizQuestionViewModel>()
    val state by quizQuestionViewModel.state.collectAsState()

    QuizQuestionScreen(
        state = state,
        onNextQuestion = { answer -> quizQuestionViewModel.setEvent(QuizQuestionUiEvent.NextQuestion(answer)) },
        publishResult = { score -> },
        cancelQuiz = { navController.navigate("quiz/start")},
        restartQuiz = { navController.navigate("quiz/start") },
        discoverPage = { navController.navigate("breeds") }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizQuestionScreen(
    state: QuizQuestionState,
    onNextQuestion: (String) -> Unit,
    publishResult: (Int) -> Unit,
    cancelQuiz: () -> Unit,
    restartQuiz: () -> Unit,
    discoverPage: () -> Unit
) {

    BackHandler(onBack = { /* Disabled */ })

    Scaffold (
        content = {
            if(state.creatingQuestions)
                CreatingQuestionsScreen()
            else if(state.quizFinished)
                QuizFinishedScreen(
                    state = state,
                    publishResult = publishResult,
                    restartQuiz = restartQuiz,
                    discoverPage = discoverPage
                )
            else {
                ShowQuestionScreen(
                    state = state,
                    showCorrectAnswer = state.showCorrectAnswer,
                    onNextQuestion = onNextQuestion,
                    cancelQuiz = cancelQuiz
                )
            }
        }
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ShowQuestionScreen(
    state: QuizQuestionState,
    showCorrectAnswer: Boolean,
    onNextQuestion: (String) -> Unit,
    cancelQuiz: () -> Unit,
) {
    val question = state.questions[state.currentQuestionIndex]
    var answer by rememberSaveable { mutableStateOf("") }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Are you sure you want to give up?") },
            confirmButton = {
                Button(onClick = {
                    showCancelDialog = false
                    cancelQuiz()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showCancelDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        QuizSessionInfo(state = state)

        CountdownTimer(timeLeft = state.timeLeft, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.fillMaxHeight(0.08F))

        BoxWithConstraints(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            SubcomposeAsyncImage(
                model = question.breedImageUrl,
                loading = { Text("Loading...") },
                error = { Text("Image loading failed") },
                contentDescription = "Breed Image",
                modifier = Modifier.fillMaxSize(),
            )
        }

        Text(text = question.text, fontSize = 18.sp, modifier = Modifier
            .padding(top = 10.dp)
            .align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.fillMaxHeight(0.05F))

        ShowOfferedAnswers(
            question = question,
            selectedAnswer = answer,
            onAnswerSelected = { selected -> answer = selected },
            showCorrectAnswer = showCorrectAnswer
        )

        Spacer(modifier = Modifier.fillMaxHeight(0.16F))

        Button(
            onClick = {
                onNextQuestion(answer)
                answer = ""
            },
            enabled = answer.isNotEmpty(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Next Question") }

        Spacer(modifier = Modifier.fillMaxHeight(0.08F))

        Button(
            onClick = { showCancelDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Cancel Quiz") }
    }
}

@Composable
fun ShowOfferedAnswers(
    question: Question,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    showCorrectAnswer: Boolean              // when true, show correct answer in green, wrong answers in red
) {
    val correctAnswer = question.correctAnswer

    Column {
        question.answers.chunked(2).forEach { rowAnswers ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowAnswers.forEach { ans ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .clickable { onAnswerSelected(ans) }
                            .border(
                                width = 2.dp,
                                color = when {
                                    showCorrectAnswer && ans == correctAnswer -> Color.Green
                                    showCorrectAnswer && ans != correctAnswer -> Color.Red
                                    ans == selectedAnswer -> MaterialTheme.colors.primary
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(4.dp)
                            ),
                        elevation = 4.dp
                    ) {
                        Text(
                            text = ans,
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            color = MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun QuizSessionInfo(
    state: QuizQuestionState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Question: ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("${state.currentQuestionIndex} of ${state.questions.size}")
                }
            }
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("Correct answers: ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                    append("${state.correctAnswers}")
                }
            }
        )
    }
}


@Composable
fun CountdownTimer(
    timeLeft: Long,
    modifier: Modifier
) {
    val animatedTimeLeft = remember { Animatable(timeLeft.toFloat()) }

    LaunchedEffect(timeLeft) {
        animatedTimeLeft.animateTo(
            targetValue = timeLeft.toFloat(),
            animationSpec = tween(
                durationMillis = 1000, // duration of one second
                easing = LinearEasing
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "Timer",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%02d:%02d", animatedTimeLeft.value.toInt() / 60, animatedTimeLeft.value.toInt() % 60),
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun CreatingQuestionsScreen() {
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

@Composable
fun QuizFinishedScreen(
    state: QuizQuestionState,
    publishResult: (Int) -> Unit,
    restartQuiz: () -> Unit,
    discoverPage: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1F))

        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.quiz_finished),
            contentDescription = "Quiz finished image",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "Correct answers: ${state.correctAnswers}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Text(
            text = "Score: ${state.score}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Button(onClick = { publishResult(state.correctAnswers) }) { Text("Publish Result") }
            Button(onClick = { restartQuiz() }) { Text("Restart Quiz") }
        }

        Button(
            onClick = { discoverPage() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) { Text("Discover More Breeds") }
    }
}

@Preview
@Composable
fun QuizFinishedScreenPreview() {
    QuizFinishedScreen(
        state = QuizQuestionState(
            creatingQuestions = false,
            questions = listOf(),
            currentQuestionIndex = 0,
            correctAnswers = 3,
            timeLeft = 300L,
            score = 80.0
        ),
        publishResult = {},
        restartQuiz = {},
        discoverPage = {}
    )
}