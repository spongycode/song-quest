package com.spongycode.songquest.ui.screen.gameplay.playing

sealed class OptionTapState {
    data object Idle : OptionTapState()
    data object Checking : OptionTapState()
    data object CorrectAnswer : OptionTapState()
    data object WrongAnswer : OptionTapState()
}