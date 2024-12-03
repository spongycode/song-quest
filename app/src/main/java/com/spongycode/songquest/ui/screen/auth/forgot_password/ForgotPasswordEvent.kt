package com.spongycode.songquest.ui.screen.auth.forgot_password

sealed interface ForgotPasswordEvent {
    data class EnteredEmail(val value: String) : ForgotPasswordEvent
    data class EnteredPassword(val value: String) : ForgotPasswordEvent
    data class EnteredOTP(val value: String) : ForgotPasswordEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : ForgotPasswordEvent
    data object TogglePasswordVisibility : ForgotPasswordEvent
    data object SendResetPasswordEmail : ForgotPasswordEvent
    data object SendChangePassword : ForgotPasswordEvent
}