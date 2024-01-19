package com.spongycode.songquest.ui.screen.auth.forgot_password

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.screen.auth.components.CustomButton
import com.spongycode.songquest.ui.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.screen.auth.components.TitleText
import com.spongycode.songquest.ui.screen.auth.forgot_password.ForgotPasswordState.*
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.Constants
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val email = viewModel.email.value
    val otp = viewModel.otp.value
    val password = viewModel.password.value
    val forgotPasswordState = viewModel.forgotPasswordState.value
    val changePasswordState = viewModel.changePasswordState.value
    val isPasswordVisible = viewModel.isPasswordVisible.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
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


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                        text = email,
                        labelText = "Email",
                        placeHolderText = "Email",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        onValueChange = { viewModel.onEvent(ForgotPasswordEvent.EnteredEmail(it)) },
                        enabled = viewModel.forgotPasswordState.value != Success
                    )


                    if (forgotPasswordState != Success) {
                        Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))
                        CustomButton(
                            onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                if (forgotPasswordState == Success) {
                                    navController.navigate("login")
                                } else if (forgotPasswordState == Idle) {
                                    viewModel.onEvent(ForgotPasswordEvent.SendResetPasswordEmail)
                                }
                            },
                            containerColor = when (forgotPasswordState) {
                                Checking -> Color.DarkGray
                                Idle -> DecentBlue
                                Error -> DecentRed
                                Success -> DecentGreen
                            },
                            contentColor = Color.Black,
                            displayText = when (forgotPasswordState) {
                                Checking -> "Sending..."
                                Idle -> "Send OTP"
                                Error -> stringResource(R.string.registration_error)
                                Success -> "Check mail 🚀"
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.height(Constants.MEDIUM_HEIGHT))
                        CustomTextField(
                            text = otp,
                            labelText = "OTP",
                            placeHolderText = "Enter OTP",
                            shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                            singleLine = true,
                            onValueChange = {
                                viewModel.onEvent(
                                    ForgotPasswordEvent.EnteredOTP(
                                        it
                                    )
                                )
                            },
                            keyboardType = KeyboardType.Number
                        )

                        CustomTextField(
                            text = password,
                            labelText = "New Password",
                            placeHolderText = "New password",
                            shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                            singleLine = true,
                            isPasswordVisible = isPasswordVisible,
                            keyboardType = KeyboardType.Password,
                            onValueChange = {
                                viewModel.onEvent(
                                    ForgotPasswordEvent.EnteredPassword(
                                        it
                                    )
                                )
                            },
                            onPasswordToggleClick = { viewModel.onEvent(ForgotPasswordEvent.TogglePasswordVisibility) }
                        )
                        Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))
                        CustomButton(
                            onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                if (changePasswordState == Success) {
                                    navController.navigateUp()
                                } else if (changePasswordState == Idle) {
                                    viewModel.onEvent(ForgotPasswordEvent.SendChangePassword)
                                }
                            },
                            containerColor = when (changePasswordState) {
                                Checking -> Color.DarkGray
                                Idle -> DecentBlue
                                Error -> DecentRed
                                Success -> DecentGreen
                            },
                            contentColor = Color.Black,
                            displayText = when (changePasswordState) {
                                Checking -> "Updating password.."
                                Idle -> "Update Password"
                                Error -> stringResource(R.string.registration_error)
                                Success -> "Success, Proceed to Login 🚀"
                            }
                        )
                    }
                }
            }
        }
    }
}