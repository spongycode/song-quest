package com.spongycode.songquest.screen.auth.forgot_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _forgotPasswordState = mutableStateOf<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState: State<ForgotPasswordState> = _forgotPasswordState

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun onEvent(event: ForgotPasswordEvent) {
        if (_forgotPasswordState.value == ForgotPasswordState.Error) {
            _forgotPasswordState.value = ForgotPasswordState.Idle
        }
        when (event) {
            is ForgotPasswordEvent.EnteredEmail -> _email.value = event.value
            ForgotPasswordEvent.Send -> sendForgotPasswordEmail()
        }
    }

    private fun validationCheck(): String? {
        return ValidationHelper.validateEmail(_email.value)
    }

    private fun sendForgotPasswordEmail() {
        val validationError = validationCheck()

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