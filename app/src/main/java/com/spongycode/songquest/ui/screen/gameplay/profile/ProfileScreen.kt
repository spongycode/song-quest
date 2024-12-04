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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.spongycode.songquest.util.Constants.REGISTER_SCREEN
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is ProfileViewEffect.Navigate -> {
                    if (it.navigateUp) {
                        navController.navigateUp()
                    }
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    it.route?.let { route ->
                        navController.navigate(route)
                    }
                }

                is ProfileViewEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = "Okay",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    Scaffold(topBar = {
        Topbar({ navController.navigateUp() }, "Profile")
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        ProfileScreen(
            modifier = Modifier
                .padding(top = it.calculateTopPadding()),
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState = ProfileUiState(),
    onEvent: (ProfileEvent) -> Unit = {}
) {
    LaunchedEffect(null) {
        onEvent(ProfileEvent.GetPersonalDetails)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            CustomTextField(
                text = uiState.email,
                labelText = "Email",
                placeHolderText = "Email",
                shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                singleLine = true,
                onValueChange = { },
                enabled = false
            )

            Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))
            CustomTextField(
                text = uiState.username,
                labelText = "Username",
                placeHolderText = "Username",
                shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                singleLine = true,
                onValueChange = { text -> onEvent(ProfileEvent.EnteredUsername(value = text)) },
            )

            Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))

            CustomButton(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if (uiState.profileState == ProfileState.Success) {
                        onEvent(ProfileEvent.Navigate(navigateUp = true, popBackStack = false))
                    } else if (uiState.profileState == ProfileState.Idle) {
                        onEvent(ProfileEvent.SendUpdateProfile)
                    }
                },
                containerColor = when (uiState.profileState) {
                    ProfileState.Checking -> Color.DarkGray
                    ProfileState.Idle -> DecentBlue
                    ProfileState.Error -> DecentRed
                    ProfileState.Success -> DecentGreen
                },
                contentColor = Color.Black,
                displayText = when (uiState.profileState) {
                    ProfileState.Checking -> "Updating profile.."
                    ProfileState.Idle -> "Update Profile"
                    ProfileState.Error -> stringResource(R.string.registration_error)
                    ProfileState.Success -> "Success, Proceed to Home"
                }
            )
        }

        CustomButton(
            onClick = {
                onEvent(
                    ProfileEvent.Navigate(
                        route = REGISTER_SCREEN,
                        navigateUp = true,
                        popBackStack = true
                    )
                )
                onEvent(ProfileEvent.Logout)
            },
            containerColor = OptionDarkRed,
            contentColor = Color.White,
            displayText = "Log out"
        )
    }
}

@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen()
}