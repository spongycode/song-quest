package com.spongycode.songquest.ui.screen.gameplay.gameover

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.gamesPlayedSession
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val gameplayRepository: GameplayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameOverUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<GameOverViewEffect>()
    val viewEffect: SharedFlow<GameOverViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: GameOverEvent) {
        when (event) {
            GameOverEvent.GetData -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        username = datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession)
                            .toString()
                    )
                }
            }

            is GameOverEvent.SaveGame -> saveGame(event.gameId)
            is GameOverEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(
                        GameOverViewEffect.Navigate(
                            route = event.route,
                            navigateUp = event.navigateUp,
                            popBackStack = event.popBackStack
                        )
                    )
                }
            }
        }
    }

    private fun saveGame(gameId: String) {
        Log.d("saveGame", "saveGame: $gameId")
        viewModelScope.launch {
            val accessToken =
                datastoreRepository.getString(DatastoreRepositoryImpl.accessTokenSession)
            try {
                val res = gameplayRepository.saveGame(
                    CheckAnswerModel(
                        gameId = gameId,
                        accessToken = accessToken
                    )
                )
                if (res?.status == "success") {
                    _uiState.value = _uiState.value.copy(
                        game = res.data?.game!!,
                        gameOverState = GameOverState.Success
                    )
                    val gamesPlayed = res.data.gamesPlayed?.toString()
                    datastoreRepository.storeString(
                        gamesPlayedSession, gamesPlayed.toString()
                    )

                } else {
                    _uiState.value = _uiState.value.copy(
                        gameOverState = GameOverState.Error
                    )
                }
            } catch (err: Exception) {
                _uiState.value = _uiState.value.copy(
                    gameOverState = GameOverState.Error
                )
            }
        }
    }
}

sealed interface GameOverEvent {
    data object GetData : GameOverEvent
    data class SaveGame(val gameId: String) : GameOverEvent
    data class Navigate(
        val route: String? = null,
        val navigateUp: Boolean = false,
        val popBackStack: Boolean = true
    ) : GameOverEvent
}

data class GameOverUiState(
    val username: String = "",
    val game: GameModel = GameModel(),
    val gameOverState: GameOverState = GameOverState.Loading
)

sealed interface GameOverViewEffect {
    data class Navigate(
        val route: String?,
        val navigateUp: Boolean,
        val popBackStack: Boolean
    ) : GameOverViewEffect
}