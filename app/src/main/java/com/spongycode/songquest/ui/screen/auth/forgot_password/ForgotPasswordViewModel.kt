package com.spongycode.songquest.ui.screen.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<ForgotPasswordViewEffect>()
    val viewEffect: SharedFlow<ForgotPasswordViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        if (_uiState.value.forgotPasswordState == ForgotPasswordState.Error) {
            _uiState.value = _uiState.value.copy(
                forgotPasswordState = ForgotPasswordState.Idle
            )
        }
        if (_uiState.value.changePasswordState == ForgotPasswordState.Error) {
            _uiState.value = _uiState.value.copy(
                changePasswordState = ForgotPasswordState.Idle
            )
        }
        when (event) {
            is ForgotPasswordEvent.EnteredEmail -> _uiState.value =
                _uiState.value.copy(email = event.value)

            is ForgotPasswordEvent.EnteredPassword -> _uiState.value =
                _uiState.value.copy(password = event.value)

            is ForgotPasswordEvent.EnteredOTP -> _uiState.value =
                _uiState.value.copy(otp = event.value)

            is ForgotPasswordEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(
                        ForgotPasswordViewEffect.Navigate(
                            route = event.route,
                            popBackStack = event.popBackStack
                        )
                    )
                }
            }

            ForgotPasswordEvent.TogglePasswordVisibility -> _uiState.value = _uiState.value.copy(
                isPasswordVisible =
                _uiState.value.isPasswordVisible.not()
            )

            ForgotPasswordEvent.SendResetPasswordEmail -> sendForgotPasswordEmail()
            ForgotPasswordEvent.SendChangePassword -> changePassword()
        }
    }

    private fun changePassword() {
        val validationError = ValidationHelper.validatePassword(_uiState.value.password)
            ?: ValidationHelper.validateOTP(_uiState.value.otp)
        viewModelScope.launch {
            if (validationError != null) {
                _viewEffect.emit(ForgotPasswordViewEffect.ShowSnackBar(message = validationError))
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                changePasswordState = ForgotPasswordState.Checking
            )
            try {
                val res = repository.changePassword(
                    UserModel(
                        email = _uiState.value.email,
                        otp = _uiState.value.otp,
                        password = _uiState.value.password
                    )
                )
                if (res?.status == "success") {
                    _uiState.value = _uiState.value.copy(
                        changePasswordState = ForgotPasswordState.Success
                    )
                } else {
                    _viewEffect.emit(ForgotPasswordViewEffect.ShowSnackBar(message = res?.message.toString()))
                    _uiState.value = _uiState.value.copy(
                        changePasswordState = ForgotPasswordState.Error
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    changePasswordState = ForgotPasswordState.Error
                )
            }
        }
    }

    private fun sendForgotPasswordEmail() {
        val validationError = ValidationHelper.validateEmail(_uiState.value.email)

        viewModelScope.launch {
            if (validationError != null) {
                _viewEffect.emit(ForgotPasswordViewEffect.ShowSnackBar(message = validationError))
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                forgotPasswordState = ForgotPasswordState.Checking
            )
            try {
                val res = repository.forgotPasswordEmail(_uiState.value.email)
                if (res?.status == "success") {
                    _uiState.value = _uiState.value.copy(
                        forgotPasswordState = ForgotPasswordState.Success
                    )
                } else {
                    _viewEffect.emit(ForgotPasswordViewEffect.ShowSnackBar(message = res?.message.toString()))
                    _uiState.value = _uiState.value.copy(
                        forgotPasswordState = ForgotPasswordState.Error
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    forgotPasswordState = ForgotPasswordState.Error
                )
            }
        }
    }
}

data class ForgotPasswordUiState(
    val email: String = "",
    val password: String = "",
    val otp: String = "",
    val isPasswordVisible: Boolean = false,
    val forgotPasswordState: ForgotPasswordState = ForgotPasswordState.Idle,
    val changePasswordState: ForgotPasswordState = ForgotPasswordState.Idle
)

sealed interface ForgotPasswordViewEffect {
    data class ShowSnackBar(val message: String) : ForgotPasswordViewEffect
    data class Navigate(val route: String, val popBackStack: Boolean = true) :
        ForgotPasswordViewEffect
}