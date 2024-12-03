package com.spongycode.songquest.ui.screen.gameplay.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.screen.gameplay.profile.components.Topbar
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.ui.theme.OptionDarkRed
import com.spongycode.songquest.util.Constants
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val profileState = viewModel.profileState.value
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = keyboardController) {
        viewModel.snackBarFlow.collectLatest { event ->
            if (event.show) {
                keyboardController?.hide()
                snackBarHostState.showSnackbar(
                    message = event.text,
                    actionLabel = "Okay",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Profile")
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        Column(
            Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column {
                CustomTextField(
                    text = viewModel.email.value,
                    labelText = "Email",
                    placeHolderText = "Email",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    onValueChange = { },
                    enabled = false
                )

                Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))
                CustomTextField(
                    text = viewModel.username.value,
                    labelText = "Username",
                    placeHolderText = "Username",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    onValueChange = { text -> viewModel.onEvent(ProfileEvent.EnteredUsername(value = text)) },
                )

                Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))

                CustomButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        if (profileState == ProfileState.Success) {
                            navController.navigateUp()
                        } else if (profileState == ProfileState.Idle) {
                            viewModel.onEvent(ProfileEvent.SendUpdateProfile)
                        }
                    },
                    containerColor = when (profileState) {
                        ProfileState.Checking -> Color.DarkGray
                        ProfileState.Idle -> DecentBlue
                        ProfileState.Error -> DecentRed
                        ProfileState.Success -> DecentGreen
                    },
                    contentColor = Color.Black,
                    displayText = when (profileState) {
                        ProfileState.Checking -> "Updating profile.."
                        ProfileState.Idle -> "Update Profile"
                        ProfileState.Error -> stringResource(R.string.registration_error)
                        ProfileState.Success -> "Success, Proceed to Home"
                    }
                )
            }

            CustomButton(
                onClick = {
                    navController.navigateUp()
                    navController.popBackStack()
                    navController.navigate("register")
                    viewModel.onEvent(ProfileEvent.Logout)
                },
                containerColor = OptionDarkRed,
                contentColor = Color.White,
                displayText = "Log out"
            )
        }
    }
}
