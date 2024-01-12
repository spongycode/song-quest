package com.spongycode.songquest.screen.auth.forgot_password

sealed class ForgotPasswordEvent {
    data class EnteredEmail(val value: String) : ForgotPasswordEvent()
    data object Send : ForgotPasswordEvent()
}
