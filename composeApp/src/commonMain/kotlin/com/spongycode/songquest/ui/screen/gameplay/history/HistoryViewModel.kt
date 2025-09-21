package com.spongycode.songquest.ui.screen.gameplay.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.repository.GameplayRepository
import com.spongycode.songquest.repository.SettingsRepository
import com.spongycode.songquest.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val gameplayRepository: GameplayRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.FetchHistoryGames -> fetchHistoryGames()
        }
    }

    private fun fetchHistoryGames() {
        viewModelScope.launch {
            val accessToken =
                settingsRepository.getString(SettingsRepositoryImpl.ACCESS_TOKEN_SESSION)
            try {
                val res = gameplayRepository.history(
                    AuthModel(accessToken = accessToken)
                )
                if (res?.status == "success") {
                    _uiState.value = _uiState.value.copy(
                        games = res.data?.games!!,
                        historyState = HistoryState.Success
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        historyState = HistoryState.Error
                    )
                }
            } catch (err: Exception) {
                _uiState.value = _uiState.value.copy(
                    historyState = HistoryState.Error
                )
            }
        }
    }
}

sealed interface HistoryEvent {
    data object FetchHistoryGames : HistoryEvent
}

data class HistoryUiState(
    val games: List<GameModel> = emptyList(),
    val historyState: HistoryState = HistoryState.Loading
)

sealed interface HistoryState {
    data object Loading : HistoryState
    data object Success : HistoryState
    data object Error : HistoryState
}