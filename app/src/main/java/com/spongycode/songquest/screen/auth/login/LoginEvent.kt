package com.spongycode.songquest.screen.auth.login

sealed class LoginEvent {
    data class EnteredEmailOrUsername(val value: String) : LoginEvent()
    data class EnteredPassword(val value: String) : LoginEvent()
    data object TogglePasswordVisibility : LoginEvent()
    data object Login : LoginEvent()
}
