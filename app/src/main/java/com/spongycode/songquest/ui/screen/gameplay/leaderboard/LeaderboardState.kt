package com.spongycode.songquest.ui.screen.gameplay.leaderboard

sealed class LeaderboardState {
    data object Loading : LeaderboardState()
    data object Success : LeaderboardState()
    data object Error : LeaderboardState()
}