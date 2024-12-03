package com.spongycode.songquest.ui.screen.auth.login

sealed interface LoginEvent {
    data class EnteredEmailOrUsername(val value: String) : LoginEvent
    data class EnteredPassword(val value: String) : LoginEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object Login : LoginEvent
}