package com.spongycode.songquest.screen.gameplay.playing

sealed class PlayingState {
    data object Idle : PlayingState()
    data object Checking : PlayingState()
    data object CorrectAnswer : PlayingState()
    data object WrongAnswer : PlayingState()
}