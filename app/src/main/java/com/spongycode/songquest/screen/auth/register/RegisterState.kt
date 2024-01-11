package com.spongycode.songquest.screen.auth.register

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Checking : RegisterState()
    data object Success : RegisterState()
    data object Error : RegisterState()
}