package com.spongycode.songquest.screen.gameplay.profile


sealed class ProfileState {
    data object Idle : ProfileState()
    data object Checking : ProfileState()
    data object Success : ProfileState()
    data object Error : ProfileState()
}