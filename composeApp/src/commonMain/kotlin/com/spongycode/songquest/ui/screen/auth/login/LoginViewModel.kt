package com.spongycode.songquest.ui.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.repository.AuthRepository
import com.spongycode.songquest.repository.SettingsRepository
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.ACCESS_TOKEN_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.EMAIL_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.GAMES_PLAYED_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.REFRESH_TOKEN_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.USERNAME_SESSION
import com.spongycode.songquest.util.ValidationHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.listOf

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<LoginViewEffect>()
    val viewEffect: SharedFlow<LoginViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        if (_uiState.value.loginState == LoginState.Error) {
            _uiState.value = _uiState.value.copy(
                loginState = LoginState.Idle
            )
        }
        when (event) {
            is LoginEvent.EnteredEmailOrUsername -> _uiState.value = _uiState.value.copy(
                emailOrUsername = event.value
            )

            is LoginEvent.EnteredPassword -> _uiState.value = _uiState.value.copy(
                password = event.value
            )

            is LoginEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(LoginViewEffect.Navigate(event.route, event.popBackStack))
                }
            }

            LoginEvent.TogglePasswordVisibility -> _uiState.value = _uiState.value.copy(
                isPasswordVisible = _uiState.value.isPasswordVisible.not()
            )

            LoginEvent.Login -> loginUser()
        }
    }

    private fun validationCheck(): String? {
        return ValidationHelper.validateEmailOrUsername(_uiState.value.emailOrUsername)
            ?: ValidationHelper.validatePassword(_uiState.value.password)
    }

    private fun loginUser() {
        val validationError = validationCheck()

        viewModelScope.launch {
            if (validationError != null) {
                _viewEffect.emit(LoginViewEffect.ShowSnackBar(message = validationError))
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                loginState = LoginState.Checking
            )
            try {
                val res = authRepository.login(
                    _uiState.value.emailOrUsername,
                    _uiState.value.password
                )
                if (res?.status == "success") {
                    settingsRepository.storeListString(
                        listOf(
                            Pair(
                                ACCESS_TOKEN_SESSION,
                                res.data?.accessToken.toString()
                            ),
                            Pair(
                                REFRESH_TOKEN_SESSION,
                                res.data?.refreshToken.toString()
                            ),
                            Pair(
                                USERNAME_SESSION,
                                res.data?.user?.username.toString()
                            ),
                            Pair(
                                EMAIL_SESSION,
                                res.data?.user?.email.toString()
                            ),
                            Pair(
                                GAMES_PLAYED_SESSION,
                                res.data?.user?.gamesPlayed.toString()
                            )
                        )
                    )
                    _uiState.value = _uiState.value.copy(
                        loginState = LoginState.Success
                    )
                } else {
                    _viewEffect.emit(LoginViewEffect.ShowSnackBar(message = res?.message.toString()))
                    _uiState.value = _uiState.value.copy(
                        loginState = LoginState.Error
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    loginState = LoginState.Error
                )
            }
        }
    }
}

data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val loginState: LoginState = LoginState.Idle
)

sealed interface LoginViewEffect {
    data class ShowSnackBar(val message: String) : LoginViewEffect
    data class Navigate(val route: String, val popBackStack: Boolean = true) : LoginViewEffect
}

sealed interface LoginEvent {
    data class EnteredEmailOrUsername(val value: String) : LoginEvent
    data class EnteredPassword(val value: String) : LoginEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object Login : LoginEvent
}

sealed interface LoginState {
    data object Idle : LoginState
    data object Checking : LoginState
    data object Success : LoginState
    data object Error : LoginState
}