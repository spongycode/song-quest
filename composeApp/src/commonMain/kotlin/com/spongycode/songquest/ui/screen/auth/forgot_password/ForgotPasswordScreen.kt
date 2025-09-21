package com.spongycode.songquest.ui.screen.auth.forgot_password

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.screen.auth.components.TitleText
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordState.Checking
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordState.Error
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordState.Idle
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordState.Success
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.ComposeLocalWrapper
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Constants.LOGIN_SCREEN
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.registration_error

@Composable
fun ForgotPasswordScreenRoot(
    viewModel: ForgotPasswordViewModel
) {
    val navController = LocalNavController.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is ForgotPasswordViewEffect.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = "Okay",
                        duration = SnackbarDuration.Short
                    )
                }

                is ForgotPasswordViewEffect.Navigate -> {
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
        ForgotPasswordScreen(
            uiState = viewModel.uiState.collectAsState().value,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    uiState: ForgotPasswordUiState = ForgotPasswordUiState(),
    onEvent: (ForgotPasswordEvent) -> Unit = {},
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

        TitleText("Reset Password 🔑")

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
                    text = uiState.email,
                    labelText = "Email",
                    placeHolderText = "Email",
                    shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                    singleLine = true,
                    onValueChange = { onEvent(ForgotPasswordEvent.EnteredEmail(it)) },
                    enabled = uiState.forgotPasswordState != Success
                )


                if (uiState.forgotPasswordState != Success) {
                    Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))
                    CustomButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            if (uiState.forgotPasswordState == Success) {
                                onEvent(ForgotPasswordEvent.Navigate(route = LOGIN_SCREEN))
                            } else if (uiState.forgotPasswordState == Idle) {
                                onEvent(ForgotPasswordEvent.SendResetPasswordEmail)
                            }
                        },
                        containerColor = when (uiState.forgotPasswordState) {
                            Checking -> Color.DarkGray
                            Idle -> DecentBlue
                            Error -> DecentRed
                            Success -> DecentGreen
                        },
                        contentColor = Color.Black,
                        displayText = when (uiState.forgotPasswordState) {
                            Checking -> "Sending..."
                            Idle -> "Send OTP"
                            Error -> stringResource(Res.string.registration_error)
                            Success -> "Check mail 🚀"
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))
                    CustomTextField(
                        text = uiState.otp,
                        labelText = "OTP",
                        placeHolderText = "Enter OTP",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        onValueChange = {
                            onEvent(
                                ForgotPasswordEvent.EnteredOTP(
                                    it
                                )
                            )
                        },
                        keyboardType = KeyboardType.Number
                    )

                    CustomTextField(
                        text = uiState.password,
                        labelText = "New Password",
                        placeHolderText = "New password",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        isPasswordVisible = uiState.isPasswordVisible,
                        keyboardType = KeyboardType.Password,
                        onValueChange = {
                            onEvent(
                                ForgotPasswordEvent.EnteredPassword(
                                    it
                                )
                            )
                        },
                        onPasswordToggleClick = { onEvent(ForgotPasswordEvent.TogglePasswordVisibility) }
                    )
                    Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))
                    CustomButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            if (uiState.changePasswordState == Success) {
                                onEvent(ForgotPasswordEvent.Navigate(route = LOGIN_SCREEN))
                            } else if (uiState.changePasswordState == Idle) {
                                onEvent(ForgotPasswordEvent.SendChangePassword)
                            }
                        },
                        containerColor = when (uiState.changePasswordState) {
                            Checking -> Color.DarkGray
                            Idle -> DecentBlue
                            Error -> DecentRed
                            Success -> DecentGreen
                        },
                        contentColor = Color.Black,
                        displayText = when (uiState.changePasswordState) {
                            Checking -> "Updating password.."
                            Idle -> "Update Password"
                            Error -> stringResource(Res.string.registration_error)
                            Success -> "Success, Proceed to Login 🚀"
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreen() {
    ComposeLocalWrapper {
        ForgotPasswordScreen()
    }
}