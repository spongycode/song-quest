package com.spongycode.songquest.screen.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.auth.components.CustomAnnotatedString
import com.spongycode.songquest.screen.auth.components.CustomButton
import com.spongycode.songquest.screen.auth.components.CustomTextField
import com.spongycode.songquest.screen.auth.components.TitleText
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.Constants
import com.spongycode.songquest.util.Fonts
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailOrUsername = viewModel.emailOrUsername.value
    val password = viewModel.password.value
    val isPasswordVisible = viewModel.isPasswordVisible.value
    val loginState = viewModel.loginState.value

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
                        text = emailOrUsername,
                        labelText = "Username/Email",
                        placeHolderText = "Username or Email",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        onValueChange = { viewModel.onEvent(LoginEvent.EnteredEmailOrUsername(it)) },
                    )

                    Spacer(modifier = Modifier.height(Constants.VERY_SMALL_HEIGHT))

                    CustomTextField(
                        text = password,
                        labelText = "Password",
                        placeHolderText = "Password",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        isPasswordVisible = isPasswordVisible,
                        keyboardType = KeyboardType.Password,
                        onValueChange = { viewModel.onEvent(LoginEvent.EnteredPassword(it)) },
                        onPasswordToggleClick = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) }
                    )

                    Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))

                    Text(
                        text = "Forgot password?",
                        color = Color(0xFF267BC4),
                        textDecoration = TextDecoration.Underline,
                        fontFamily = Fonts.poppinsFamily,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("forgotpassword")
                            }
                    )

                    Spacer(modifier = Modifier.height(Constants.VERY_LARGE_HEIGHT))

                    CustomButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            if (loginState == LoginState.Success) {
                                navController.popBackStack()
                                navController.navigate("home")
                            } else if (loginState == LoginState.Idle) {
                                viewModel.onEvent(LoginEvent.Login)
                            }
                        },
                        containerColor = when (loginState) {
                            LoginState.Checking -> Color.DarkGray
                            LoginState.Idle -> DecentBlue
                            LoginState.Error -> DecentRed
                            LoginState.Success -> DecentGreen
                        },
                        contentColor = Color.Black,
                        displayText = when (loginState) {
                            LoginState.Checking -> "Logging in..."
                            LoginState.Idle -> "Login"
                            LoginState.Error -> stringResource(R.string.registration_error)
                            LoginState.Success -> stringResource(R.string.start_playing)
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
                    navController.popBackStack()
                    navController.navigate("register")
                }
            )
        }
    }
}

