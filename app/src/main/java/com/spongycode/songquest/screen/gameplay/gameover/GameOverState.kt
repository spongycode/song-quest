package com.spongycode.songquest.screen.gameplay.gameover

sealed class GameOverState {
    data object Loading : GameOverState()
    data object Success : GameOverState()
    data object Error : GameOverState()
}