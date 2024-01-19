package com.spongycode.songquest.ui.screen.gameplay.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val gameplayRepository: GameplayRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _historyState = mutableStateOf<HistoryState>(HistoryState.Loading)
    val historyState: State<HistoryState> = _historyState


    private val _games = mutableStateListOf<GameModel>()
    val games: SnapshotStateList<GameModel> = _games

    init {
        fetchHistoryGames()
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
                    _games.clear()
                    _games.addAll(res.data?.games!!)
                    _historyState.value = HistoryState.Success
                } else {
                    _historyState.value = HistoryState.Error
                }
            } catch (err: Exception) {
                _historyState.value = HistoryState.Error
            }
        }
    }
}