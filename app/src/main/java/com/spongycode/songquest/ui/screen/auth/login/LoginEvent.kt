package com.spongycode.songquest.ui.screen.auth.login

sealed interface LoginEvent {
    data class EnteredEmailOrUsername(val value: String) : LoginEvent
    data class EnteredPassword(val value: String) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object Login : LoginEvent
    data object NavigateToHome : LoginEvent
    data object NavigateToRegister : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
}