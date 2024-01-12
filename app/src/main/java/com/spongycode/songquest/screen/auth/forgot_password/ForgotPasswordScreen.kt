package com.spongycode.songquest.screen.auth.forgot_password

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.auth.components.CustomTextField
import com.spongycode.songquest.screen.auth.forgot_password.ForgotPasswordState.*
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
    val forgotPasswordState = viewModel.forgotPasswordState.value

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val snackBarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )

    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
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

            Text(text = "Forgot Password", fontSize = 35.sp, fontWeight = FontWeight.W800)

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
                    )


                    Spacer(modifier = Modifier.height(Constants.VERY_LARGE_HEIGHT))

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            if (forgotPasswordState == Success) {
                                navController.navigate("login")
                                launcher.launch(intent)
                            } else if (forgotPasswordState == Idle) {
                                viewModel.onEvent(ForgotPasswordEvent.Send)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (forgotPasswordState) {
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
                            text = when (forgotPasswordState) {
                                Checking -> "Sending..."
                                Idle -> "Send"
                                Error -> stringResource(R.string.registration_error)
                                Success -> "Check mail 🚀"
                            },
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}