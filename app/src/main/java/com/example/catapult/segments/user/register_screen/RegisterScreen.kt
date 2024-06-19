package com.example.catapult.segments.user.register_screen

import com.example.catapult.segments.user.register_screen.RegisterContract.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.catapult.core.compose.UserForm

fun NavGraphBuilder.registerScreen(
    route: String,
) = composable(route = route) {

    val registerViewModel = hiltViewModel<RegisterViewModel>()

    RegisterScreen(
        onRegister = { firstName, lastName, nickname, email ->
            registerViewModel.setEvent(RegisterEvent.Register(firstName, lastName, nickname, email))
        }
    )
}

@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") }
            )
        },
        content = { paddingValues ->
            UserForm(
                firstName = firstName,
                onFirstNameChange = { firstName = it },
                lastName = lastName,
                onLastNameChange = { lastName = it },
                nickname = nickname,
                onNicknameChange = { nickname = it },
                email = email,
                onEmailChange = { email = it },
                buttonText = "Register",
                onClick = { onRegister(firstName.text, lastName.text, nickname.text, email.text) },
                paddingValues = paddingValues
            )
        }
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegister = { _, _, _, _ -> }
    )
}
