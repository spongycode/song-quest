package com.spongycode.songquest.ui.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Checking
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Error
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.accessTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.emailSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.gamesPlayedSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.refreshTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.usernameSession
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Idle
import com.spongycode.songquest.ui.screen.auth.register.RegisterState.Success
import com.spongycode.songquest.ui.screen.auth.register.RegisterViewEffect.ShowSnackBar
import com.spongycode.songquest.util.Constants.HOME_SCREEN
import com.spongycode.songquest.util.Constants.LOGIN_SCREEN
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<RegisterViewEffect>()
    val viewEffect: SharedFlow<RegisterViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        if (_uiState.value.registerState == Error) {
            _uiState.value = _uiState.value.copy(
                registerState = Idle
            )
        }
        when (event) {
            is RegisterEvent.EnteredUsername -> {
                _uiState.value = _uiState.value.copy(
                    username = event.value
                )
            }

            is RegisterEvent.EnteredEmail -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value
                )
            }

            is RegisterEvent.EnteredPassword -> {
                _uiState.value = _uiState.value.copy(
                    password = event.value
                )
            }

            is RegisterEvent.TogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    isPasswordVisible = _uiState.value.isPasswordVisible.not()
                )
            }

            is RegisterEvent.Register -> registerUser()
            RegisterEvent.NavigateToHome -> {
                viewModelScope.launch {
                    _viewEffect.emit(RegisterViewEffect.Navigate(route = HOME_SCREEN))
                }
            }

            RegisterEvent.NavigateToLogin -> {
                viewModelScope.launch {
                    _viewEffect.emit(RegisterViewEffect.Navigate(route = LOGIN_SCREEN))
                }
            }
        }
    }

    private fun validationCheck(): String? {
        return ValidationHelper.validateUsername(_uiState.value.username)
            ?: ValidationHelper.validateEmail(_uiState.value.email)
            ?: ValidationHelper.validatePassword(_uiState.value.password)
    }

    private fun registerUser() {
        val validationError = validationCheck()

        viewModelScope.launch {
            if (validationError != null) {
                _viewEffect.emit(ShowSnackBar(message = validationError))
                return@launch
            }
            _uiState.value = _uiState.value.copy(
                registerState = Checking
            )
            try {
                val res = authRepository.register(
                    _uiState.value.username,
                    _uiState.value.email,
                    _uiState.value.password
                )
                if (res?.status == "success") {
                    datastoreRepository.storeListString(
                        listOf(
                            Pair(
                                accessTokenSession,
                                res.data?.accessToken.toString()
                            ),
                            Pair(
                                refreshTokenSession,
                                res.data?.refreshToken.toString()
                            ),
                            Pair(
                                usernameSession,
                                res.data?.user?.username.toString()
                            ),
                            Pair(
                                emailSession,
                                res.data?.user?.email.toString()
                            ),
                            Pair(
                                gamesPlayedSession,
                                0.toString()
                            )
                        )
                    )
                    _uiState.value = _uiState.value.copy(
                        registerState = Success
                    )
                } else {
                    _viewEffect.emit(ShowSnackBar(message = res?.message.toString()))
                    _uiState.value = _uiState.value.copy(
                        registerState = Error
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    registerState = Error
                )
            }
        }
    }
}

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val registerState: RegisterState = Idle
)

sealed interface RegisterViewEffect {
    data class ShowSnackBar(val message: String) : RegisterViewEffect
    data class Navigate(val route: String, val popBackStack: Boolean = true) : RegisterViewEffect
}