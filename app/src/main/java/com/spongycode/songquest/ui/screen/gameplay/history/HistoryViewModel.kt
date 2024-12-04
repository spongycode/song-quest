package com.spongycode.songquest.ui.screen.gameplay.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val gameplayRepository: GameplayRepository,
    private val datastoreRepository: DatastoreRepository
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
                datastoreRepository.getString(DatastoreRepositoryImpl.accessTokenSession)
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