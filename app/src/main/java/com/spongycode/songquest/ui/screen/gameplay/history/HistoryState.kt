package com.spongycode.songquest.ui.screen.gameplay.history

sealed class HistoryState {
    data object Loading : HistoryState()
    data object Success : HistoryState()
    data object Error : HistoryState()
}