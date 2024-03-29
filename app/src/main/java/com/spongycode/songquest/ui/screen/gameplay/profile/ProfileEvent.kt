package com.spongycode.songquest.ui.screen.gameplay.profile

sealed class ProfileEvent {
    data class EnteredUsername(val value: String) : ProfileEvent()
    data object SendUpdateProfile : ProfileEvent()
    data object Logout : ProfileEvent()
}
