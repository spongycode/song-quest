package com.spongycode.songquest.screen.auth.register

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.auth.register.RegisterEvent.*
import com.spongycode.songquest.screen.auth.register.RegisterState.*
import com.spongycode.songquest.screen.auth.components.CustomTextField
import com.spongycode.songquest.ui.theme.DecentBlue
import com.spongycode.songquest.ui.theme.DecentGreen
import com.spongycode.songquest.ui.theme.DecentRed
import com.spongycode.songquest.util.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val fullName = viewModel.fullName.value
    val username = viewModel.username.value
    val email = viewModel.email.value
    val password = viewModel.password.value
    val isPasswordVisible = viewModel.isPasswordVisible.value
    val registerState = viewModel.registerState.value

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

    if (viewModel.shouldNavigateToHome.value) {
        navController.popBackStack()
        navController.navigate("home")
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

            Text(text = "Register", fontSize = 35.sp, fontWeight = FontWeight.W800)

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
                        text = fullName,
                        labelText = "Name",
                        placeHolderText = "Name",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        onValueChange = { viewModel.onEvent(EnteredFullName(it)) },
                    )

                    Spacer(modifier = Modifier.height(Constants.VERY_SMALL_HEIGHT))

                    CustomTextField(
                        text = username,
                        labelText = "Username",
                        placeHolderText = "Username",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        onValueChange = { viewModel.onEvent(EnteredUsername(it)) },
                    )

                    Spacer(modifier = Modifier.height(Constants.VERY_SMALL_HEIGHT))

                    CustomTextField(
                        text = email,
                        labelText = "Email",
                        placeHolderText = "Email",
                        shape = RoundedCornerShape(Constants.CORNER_RADIUS_PERCENTAGE),
                        singleLine = true,
                        keyboardType = KeyboardType.Email,
                        onValueChange = { viewModel.onEvent(EnteredEmail(it)) },
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
                        onValueChange = { viewModel.onEvent(EnteredPassword(it)) },
                        onPasswordToggleClick = { viewModel.onEvent(TogglePasswordVisibility) }
                    )

                    Spacer(modifier = Modifier.height(Constants.VERY_LARGE_HEIGHT))

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            if (registerState == Success) {
                                navController.popBackStack()
                                navController.navigate("home")
                            } else if (registerState == Idle) {
                                viewModel.onEvent(Register)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (registerState) {
                                Checking -> Color.DarkGray
                                Idle -> DecentBlue
                                Error -> DecentRed
                                Success -> DecentGreen
                            },
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            color = Color.White,
                            modifier = Modifier.padding(8.dp),
                            text = when (registerState) {
                                Checking -> stringResource(R.string.registering)
                                Idle -> stringResource(R.string.register)
                                Error -> stringResource(R.string.registration_error)
                                Success -> stringResource(R.string.start_playing)
                            },
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Constants.LARGE_HEIGHT))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append("Already have an account? ")
                    }
                    pushStringAnnotation(
                        tag = "login",
                        annotation = "Login here"
                    )
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF267BC4), textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Login here")
                    }
                    pop()
                },

                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                        navController.navigate("login")
                    }
            )
        }
    }
}

