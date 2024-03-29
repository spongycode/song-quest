package com.spongycode.songquest.ui.screen.auth.register

sealed class RegisterEvent {
    data class EnteredUsername(val value: String) : RegisterEvent()
    data class EnteredEmail(val value: String) : RegisterEvent()
    data class EnteredPassword(val value: String) : RegisterEvent()
    data object TogglePasswordVisibility : RegisterEvent()
    data object Register : RegisterEvent()
}
