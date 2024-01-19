package com.spongycode.songquest.ui.screen.gameplay.gameover

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.gamesPlayedSession
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameOverViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val gameplayRepository: GameplayRepository
) : ViewModel() {

    private val _gameOverState = mutableStateOf<GameOverState>(GameOverState.Loading)
    val gameOverState: State<GameOverState> = _gameOverState

    private val _game = mutableStateOf(GameModel(null, null, null, emptyList(), null, null))
    val game: State<GameModel> = _game

    private val _username = mutableStateOf("username")
    val username: State<String> = _username

    init {
        viewModelScope.launch {
            _username.value =
                datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession).toString()
        }
    }

    fun saveGame(gameId: String) {
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
                    _game.value = res.data?.game!!
                    _gameOverState.value = GameOverState.Success
                    val gamesPlayed = res.data.gamesPlayed?.toString()
                    datastoreRepository.storeString(
                        gamesPlayedSession, gamesPlayed.toString()
                    )

                } else {
                    _gameOverState.value = GameOverState.Error
                }
            } catch (err: Exception) {
                _gameOverState.value = GameOverState.Error
            }
        }
    }
}