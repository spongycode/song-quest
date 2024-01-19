package com.spongycode.songquest.ui.screen.auth.login


sealed class LoginState {
    data object Idle : LoginState()
    data object Checking : LoginState()
    data object Success : LoginState()
    data object Error : LoginState()
}