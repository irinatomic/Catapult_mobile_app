package com.example.catapult.segments.user.profile_edit_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catapult.R
import com.example.catapult.core.compose.UserForm
import com.example.catapult.data.datastore.UserData
import com.example.catapult.segments.user.profile_edit_screen.ProfileEditContract.*

fun NavGraphBuilder.profileEditScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {

    val profileEditViewModel = hiltViewModel<ProfileEditViewModel>()
    val state by profileEditViewModel.state.collectAsState()

    ProfileEditScreen(
        state = state,
        onBack = { navController.popBackStack() },
        onSave = { firstName, lastName, nickname, email ->
            profileEditViewModel.setEvent(ProfileEditEvent.UpdateProfile(firstName, lastName, nickname, email))
            navController.navigate("profile")
        }
    )
}

@Composable
fun ProfileEditScreen(
    state: ProfileEditState,
    onBack: () -> Unit,
    onSave: (String, String, String, String) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                title = { Text("Edit Profile") }
            )
        },

        content = { paddingValues ->

            if (state.fetchingData)
                Text("Loading...", modifier = Modifier.padding(paddingValues))
            else {

                // mutableStateOf is a Composable function that creates a state object that can be read and modified
                var firstName by remember { mutableStateOf(TextFieldValue(state.userData.firstName)) }
                var lastName by remember { mutableStateOf(TextFieldValue(state.userData.lastName)) }
                var nickname by remember { mutableStateOf(TextFieldValue(state.userData.nickname)) }
                var email by remember { mutableStateOf(TextFieldValue(state.userData.email)) }

                UserForm(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    nickname = nickname,
                    onNicknameChange = { nickname = it },
                    email = email,
                    onEmailChange = { email = it },

                    buttonText = "Edit Profile",
                    onClick = { onSave(firstName.text, lastName.text, nickname.text, email.text) },
                    paddingValues = paddingValues
                )
            }
        }
    )
}

@Preview
@Composable
fun ProfileEditScreenPreview() {
    ProfileEditScreen(
        state = ProfileEditState(
            userData = UserData("John", "Doe", "john_doe", "john@email.com"),
            fetchingData = false
        ),
        onBack = {},
        onSave = { _, _, _, _ -> }
    )
}