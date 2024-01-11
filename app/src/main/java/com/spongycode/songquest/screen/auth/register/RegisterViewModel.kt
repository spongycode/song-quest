package com.spongycode.songquest.screen.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.screen.auth.register.RegisterState.*
import com.spongycode.songquest.screen.ui_events.SnackBarEvent
import com.spongycode.songquest.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _fullName = mutableStateOf("")
    val fullName: State<String> = _fullName

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    private val _registerState = mutableStateOf<RegisterState>(Idle)
    val registerState: State<RegisterState> = _registerState

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(event: RegisterEvent) {
        if (_registerState.value == Error) {
            _registerState.value = Idle
        }
        when (event) {
            is RegisterEvent.EnteredFullName -> _fullName.value = event.value
            is RegisterEvent.EnteredUsername -> _username.value = event.value
            is RegisterEvent.EnteredEmail -> _email.value = event.value
            is RegisterEvent.EnteredPassword -> _password.value = event.value
            is RegisterEvent.TogglePasswordVisibility -> _isPasswordVisible.value =
                !_isPasswordVisible.value

            is RegisterEvent.Register -> registerUser()
        }
    }

    private fun validationCheck(): String? {
        return ValidationHelper.validateFullName(_fullName.value)
            ?: ValidationHelper.validateUsername(_username.value)
            ?: ValidationHelper.validateEmail(_email.value)
            ?: ValidationHelper.validatePassword(_password.value)
    }

    private fun registerUser() {
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

            _registerState.value = Checking
            try {
                val res = repository.register(
                    _fullName.value,
                    _username.value,
                    _email.value,
                    _password.value
                )
                if (res?.status == "success") {
                    _registerState.value = Success
                } else {
                    _snackBarFlow.emit(
                        SnackBarEvent(
                            show = true,
                            text = res?.message.toString()
                        )
                    )
                    _registerState.value = Error
                }
            } catch (_: Exception) {
                _registerState.value = Error
            }
        }
    }
}
