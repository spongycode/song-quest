package com.spongycode.songquest.screen.auth.forgot_password


sealed class ForgotPasswordState {
    data object Idle : ForgotPasswordState()
    data object Checking : ForgotPasswordState()
    data object Success : ForgotPasswordState()
    data object Error : ForgotPasswordState()
}