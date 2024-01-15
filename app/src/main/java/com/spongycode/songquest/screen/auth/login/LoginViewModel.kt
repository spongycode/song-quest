package com.spongycode.songquest.screen.auth.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.accessTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.emailSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.gamesPlayedSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.refreshTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.usernameSession
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.screen.ui_events.SnackBarEvent
import com.spongycode.songquest.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _emailOrUsername = mutableStateOf("")
    val emailOrUsername: State<String> = _emailOrUsername

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    private val _loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val loginState: State<LoginState> = _loginState

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        if (_loginState.value == LoginState.Error) {
            _loginState.value = LoginState.Idle
        }
        when (event) {
            is LoginEvent.EnteredEmailOrUsername -> _emailOrUsername.value = event.value
            is LoginEvent.EnteredPassword -> _password.value = event.value
            is LoginEvent.TogglePasswordVisibility -> _isPasswordVisible.value =
                !_isPasswordVisible.value

            is LoginEvent.Login -> loginUser()
        }
    }

    private fun validationCheck(): String? {
        return ValidationHelper.validateEmailOrUsername(_emailOrUsername.value)
            ?: ValidationHelper.validatePassword(_password.value)
    }

    private fun loginUser() {
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

            _loginState.value = LoginState.Checking
            try {
                val res = authRepository.login(
                    _emailOrUsername.value,
                    _password.value
                )
                if (res?.status == "success") {
                    datastoreRepository.storeString(
                        key = accessTokenSession,
                        value = res.data?.accessToken.toString()
                    )
                    datastoreRepository.storeString(
                        key = refreshTokenSession,
                        value = res.data?.refreshToken.toString()
                    )
                    datastoreRepository.storeString(
                        key = usernameSession,
                        value = res.data?.user?.username.toString()
                    )
                    datastoreRepository.storeString(
                        key = emailSession,
                        value = res.data?.user?.email.toString()
                    )
                    datastoreRepository.storeString(
                        key = gamesPlayedSession,
                        value = res.data?.user?.gamesPlayed.toString()
                    )
                    _loginState.value = LoginState.Success
                } else {
                    _snackBarFlow.emit(
                        SnackBarEvent(
                            show = true,
                            text = res?.message.toString()
                        )
                    )
                    _loginState.value = LoginState.Error
                }
            } catch (_: Exception) {
                _loginState.value = LoginState.Error
            }
        }
    }
}