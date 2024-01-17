package com.spongycode.songquest.screen.auth.forgot_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.screen.ui_events.SnackBarEvent
import com.spongycode.songquest.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _otp = mutableStateOf("")
    val otp: State<String> = _otp

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    private val _forgotPasswordState = mutableStateOf<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: State<ForgotPasswordState> = _forgotPasswordState

    private val _changePasswordState = mutableStateOf<ForgotPasswordState>(ForgotPasswordState.Idle)
    val changePasswordState: State<ForgotPasswordState> = _changePasswordState

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        if (_forgotPasswordState.value == ForgotPasswordState.Error) {
            _forgotPasswordState.value = ForgotPasswordState.Idle
        }
        if (_changePasswordState.value == ForgotPasswordState.Error) {
            _changePasswordState.value = ForgotPasswordState.Idle
        }
        when (event) {
            is ForgotPasswordEvent.EnteredEmail -> _email.value = event.value
            is ForgotPasswordEvent.EnteredPassword -> _password.value = event.value
            is ForgotPasswordEvent.EnteredOTP -> _otp.value = event.value
            is ForgotPasswordEvent.TogglePasswordVisibility -> _isPasswordVisible.value =
                !_isPasswordVisible.value

            is ForgotPasswordEvent.SendResetPasswordEmail -> sendForgotPasswordEmail()
            ForgotPasswordEvent.SendChangePassword -> changePassword()
        }
    }

    private fun changePassword() {
        val validationError = ValidationHelper.validatePassword(_password.value)
            ?: ValidationHelper.validateOTP(_otp.value)
        viewModelScope.launch {
            if (validationError != null) {
                _snackBarFlow.emit(
                    SnackBarEvent(
                        show = true,
                        text = validationError
                    )
                )
                return@launch
            }

            _changePasswordState.value = ForgotPasswordState.Checking
            try {
                val res = repository.changePassword(
                    UserModel(
                        email = email.value, otp = otp.value, password = password.value
                    )
                )
                if (res?.status == "success") {
                    _changePasswordState.value = ForgotPasswordState.Success
                } else {
                    _snackBarFlow.emit(
                        SnackBarEvent(
                            show = true,
                            text = res?.message.toString()
                        )
                    )
                    _changePasswordState.value = ForgotPasswordState.Error
                }
            } catch (_: Exception) {
                _changePasswordState.value = ForgotPasswordState.Error
            }
        }
    }

    private fun sendForgotPasswordEmail() {
        val validationError = ValidationHelper.validateEmail(_email.value)

        viewModelScope.launch {
            if (validationError != null) {
                _snackBarFlow.emit(
                    SnackBarEvent(
                        show = true,
                        text = validationError
                    )
                )
                return@launch
            }

            _forgotPasswordState.value = ForgotPasswordState.Checking
            try {
                val res = repository.forgotPasswordEmail(_email.value)
                if (res?.status == "success") {
                    _forgotPasswordState.value = ForgotPasswordState.Success
                } else {
                    _snackBarFlow.emit(
                        SnackBarEvent(
                            show = true,
                            text = res?.message.toString()
                        )
                    )
                    _forgotPasswordState.value = ForgotPasswordState.Error
                }
            } catch (_: Exception) {
                _forgotPasswordState.value = ForgotPasswordState.Error
            }
        }
    }
}