package com.spongycode.songquest.ui.screen.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.auth.components.CustomAnnotatedString
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.screen.auth.components.TitleText
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.EnteredEmail
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.EnteredPassword
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.EnteredUsername
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.NavigateToHome
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.NavigateToLogin
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.Register
import com.spongycode.songquest.ui.screen.auth.register.RegisterEvent.TogglePasswordVisibility
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Checking
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Error
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Idle
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Success
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.ComposeLocalWrapper
import com.spongycode.songquest.util.Constants
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is RegisterViewEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = "Okay",
                        duration = SnackbarDuration.Short
                    )
                }

                is RegisterViewEffect.Navigate -> {
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    navController.navigate(it.route)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) {
        RegisterScreen(
            modifier = Modifier.padding(it),
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState = RegisterUiState(),
    onEvent: (RegisterEvent) -> Unit = {},
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))

        TitleText("Register 🚀")
        Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                CustomTextField(
                    text = uiState.username,
                    labelText = "Username",
                    placeHolderText = "Username",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    onValueChange = { onEvent(EnteredUsername(it)) },
                )

                Spacer(modifier = Modifier.height(Constants.VERY_SMALL_HEIGHT))

                CustomTextField(
                    text = uiState.email,
                    labelText = "Email",
                    placeHolderText = "Email",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    keyboardType = KeyboardType.Email,
                    onValueChange = { onEvent(EnteredEmail(it)) },
                )

                Spacer(modifier = Modifier.height(Constants.VERY_SMALL_HEIGHT))

                CustomTextField(
                    text = uiState.password,
                    labelText = "Password",
                    placeHolderText = "Password",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    isPasswordVisible = uiState.isPasswordVisible,
                    keyboardType = KeyboardType.Password,
                    onValueChange = { onEvent(EnteredPassword(it)) },
                    onPasswordToggleClick = { onEvent(TogglePasswordVisibility) }
                )

                Spacer(modifier = Modifier.height(Constants.VERY_LARGE_HEIGHT))

                CustomButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        if (uiState.registerState == Success) {
                            onEvent(NavigateToHome)
                        } else if (uiState.registerState == Idle) {
                            onEvent(Register)
                        }
                    },
                    containerColor = when (uiState.registerState) {
                        Checking -> Color.DarkGray
                        Idle -> DecentBlue
                        Error -> DecentRed
                        Success -> DecentGreen
                    },
                    contentColor = Color.Black,
                    displayText = when (uiState.registerState) {
                        Checking -> stringResource(R.string.registering)
                        Idle -> stringResource(R.string.register)
                        Error -> stringResource(R.string.registration_error)
                        Success -> stringResource(R.string.start_playing)
                    }
                )

            }
        }

        Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))

        CustomAnnotatedString(
            str1 = "Already have an account? ",
            tag = "login",
            str2 = "Login here",
            onClick = {
                onEvent(NavigateToLogin)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    ComposeLocalWrapper {
        RegisterScreen()
    }
}
