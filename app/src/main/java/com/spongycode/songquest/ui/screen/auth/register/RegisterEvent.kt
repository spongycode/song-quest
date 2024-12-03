package com.spongycode.songquest.ui.screen.auth.register

sealed interface RegisterEvent {
    data class EnteredUsername(val value: String) : RegisterEvent
    data class EnteredEmail(val value: String) : RegisterEvent
    data class EnteredPassword(val value: String) : RegisterEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : RegisterEvent
    data object TogglePasswordVisibility : RegisterEvent
    data object Register : RegisterEvent
}
