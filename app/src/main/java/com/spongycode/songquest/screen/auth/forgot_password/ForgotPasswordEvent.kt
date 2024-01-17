package com.spongycode.songquest.screen.auth.forgot_password

sealed class ForgotPasswordEvent {
    data class EnteredEmail(val value: String) : ForgotPasswordEvent()
    data class EnteredPassword(val value: String) : ForgotPasswordEvent()
    data class EnteredOTP(val value: String) : ForgotPasswordEvent()
    data object TogglePasswordVisibility : ForgotPasswordEvent()
    data object SendResetPasswordEmail : ForgotPasswordEvent()
    data object SendChangePassword : ForgotPasswordEvent()
}
