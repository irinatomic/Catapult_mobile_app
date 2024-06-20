package com.example.catapult.core.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserForm(
    firstName: TextFieldValue,
    onFirstNameChange: (TextFieldValue) -> Unit,
    lastName: TextFieldValue,
    onLastNameChange: (TextFieldValue) -> Unit,
    nickname: TextFieldValue,
    onNicknameChange: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,

    buttonText: String,
    onClick: () -> Unit,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 42.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            LineTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = "First Name",
                regex = Regex("^[a-zA-Z]*$") // only letters
            )

            LineTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = "Last Name",
                regex = Regex("^[a-zA-Z]*$") // only letters
            )

            LineTextField(
                value = nickname,
                onValueChange = onNicknameChange,
                label = "Nickname",
                regex = Regex("^[a-zA-Z0-9_]*$") // only letters, numbers, and underscores
            )

            LineTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            )

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text(text = buttonText, color = Color.White) }
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
        Text(text = label, style = TextStyle(
            fontSize = 16.sp,
            color = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface)
        )

        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                isError = !regex.matches(it.text)
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .height(24.dp) // Adjust the height as needed
        )

        Divider(color = Color.Gray, thickness = 1.dp)
    }
}