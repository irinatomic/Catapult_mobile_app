package com.example.catapult.segments.user.register_screen

import com.example.catapult.segments.user.register_screen.RegisterContract.*
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Register", style = MaterialTheme.typography.h4, modifier = Modifier.align(Alignment.CenterHorizontally))

            LineTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                regex = Regex("^[a-zA-Z]*$")        // only letters
            )

            LineTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                regex = Regex("^[a-zA-Z]*$")        // only letters
            )

            LineTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = "Nickname",
                regex = Regex("^[a-zA-Z0-9_]*$")    // only letters, numbers, and underscores
            )

            LineTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            )
        }

        Button(
            onClick = { onRegister(firstName.text, lastName.text, nickname.text, email.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text("Register")
        }
    }
}

@Composable
fun LineTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    regex: Regex,
) {
    var isError by remember { mutableStateOf(false) }

    Column {
        Text(text = label, style = TextStyle(fontSize = 16.sp, color = if (isError) Color.Red else Color.Gray))
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                isError = !regex.matches(it.text)
            },
            textStyle = TextStyle(fontSize = 18.sp, color = if (isError) Color.Red else Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .height(24.dp) // Adjust the height as needed
        )
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegister = { _, _, _, _ -> })
}
