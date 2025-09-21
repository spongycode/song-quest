package com.spongycode.songquest.ui.screen.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.auth.components.CustomAnnotatedString
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.screen.auth.components.TitleText
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.ComposeLocalWrapper
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.FORGOT_PASSWORD_SCREEN
import com.spongycode.songquest.util.Constants.HOME_SCREEN
import com.spongycode.songquest.util.Constants.REGISTER_SCREEN
import com.spongycode.songquest.util.Fonts.poppinsFamily
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.registration_error
import song_quest.composeapp.generated.resources.start_playing

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel
) {
    val navController = LocalNavController.current
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is LoginViewEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = "Okay",
                        duration = SnackbarDuration.Short
                    )
                }

                is LoginViewEffect.Navigate -> {
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
        LoginScreen(
            modifier = Modifier.padding(it),
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState = LoginUiState(),
    onEvent: (LoginEvent) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))

        TitleText("Login 🔐")

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
                    text = uiState.emailOrUsername,
                    labelText = "Username/Email",
                    placeHolderText = "Username or Email",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    onValueChange = { onEvent(LoginEvent.EnteredEmailOrUsername(it)) },
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
                    onValueChange = { onEvent(LoginEvent.EnteredPassword(it)) },
                    onPasswordToggleClick = { onEvent(LoginEvent.TogglePasswordVisibility) }
                )

                Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))

                Text(
                    text = "Forgot password?",
                    color = Color(0xFF267BC4),
                    textDecoration = TextDecoration.Underline,
                    fontFamily = poppinsFamily(),
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .clickable {
                            onEvent(
                                LoginEvent.Navigate(
                                    route = FORGOT_PASSWORD_SCREEN,
                                    popBackStack = false
                                )
                            )
                        }
                )

                Spacer(modifier = Modifier.height(Constants.VERY_LARGE_HEIGHT))

                CustomButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        if (uiState.loginState == LoginState.Success) {
                            onEvent(LoginEvent.Navigate(route = HOME_SCREEN))
                        } else if (uiState.loginState == LoginState.Idle) {
                            onEvent(LoginEvent.Login)
                        }
                    },
                    containerColor = when (uiState.loginState) {
                        LoginState.Checking -> Color.DarkGray
                        LoginState.Idle -> DecentBlue
                        LoginState.Error -> DecentRed
                        LoginState.Success -> DecentGreen
                    },
                    contentColor = Color.Black,
                    displayText = when (uiState.loginState) {
                        LoginState.Checking -> "Logging in..."
                        LoginState.Idle -> "Login"
                        LoginState.Error -> stringResource(Res.string.registration_error)
                        LoginState.Success -> stringResource(Res.string.start_playing)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))

        CustomAnnotatedString(
            str1 = "Don't have an account? ",
            tag = "register",
            str2 = "Register here",
            onClick = {
                onEvent(LoginEvent.Navigate(route = REGISTER_SCREEN))
            }
        )
    }

}

@Preview
@Composable
private fun PreviewLoginScreen() {
    ComposeLocalWrapper {
        LoginScreen()
    }
}