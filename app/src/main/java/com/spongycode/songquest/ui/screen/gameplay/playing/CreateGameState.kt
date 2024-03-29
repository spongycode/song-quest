package com.spongycode.songquest.ui.screen.gameplay.playing

sealed class CreateGameState {
    data object Fetching : CreateGameState()
    data object Success : CreateGameState()
    data object Error : CreateGameState()
}