package com.spongycode.songquest.ui.screen.gameplay.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.LeaderboardUsersModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import com.spongycode.songquest.util.CategoryConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val gameplayRepository: GameplayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LeaderboardEvent) {
        when (event) {
            LeaderboardEvent.FetchLeaderboard -> fetchLeaderboard()
            is LeaderboardEvent.ChangeCategory -> changeSelectedCategory(event.category)
        }
    }

    private fun changeSelectedCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category
        )
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            val accessToken =
                datastoreRepository.getString(DatastoreRepositoryImpl.accessTokenSession)
            try {
                val res = gameplayRepository.leaderboard(
                    AuthModel(
                        accessToken = accessToken,
                        categories = CategoryConvertor.giveAllCategories()
                    )
                )
                if (res?.status == "success") {
                    res.data?.forEach { item ->
                        val updatedDatabase =
                            _uiState.value.leaderboardDatabase.toMutableMap().apply {
                                this[item.category] = item.users
                            }
                        _uiState.value = _uiState.value.copy(
                            leaderboardDatabase = updatedDatabase
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        leaderboardState = LeaderboardState.Success
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        leaderboardState = LeaderboardState.Error
                    )
                }
            } catch (err: Exception) {
                _uiState.value = _uiState.value.copy(
                    leaderboardState = LeaderboardState.Error
                )
            }
        }
    }
}

data class LeaderboardUiState(
    val leaderboardState: LeaderboardState = LeaderboardState.Loading,
    val leaderboardDatabase: Map<String, List<LeaderboardUsersModel>> = mutableMapOf(),
    val selectedCategory: String = CategoryConvertor.giveAllCategories()[0]
)

sealed interface LeaderboardEvent {
    data class ChangeCategory(val category: String) : LeaderboardEvent
    data object FetchLeaderboard : LeaderboardEvent
}

sealed interface LeaderboardState {
    data object Loading : LeaderboardState
    data object Success : LeaderboardState
    data object Error : LeaderboardState
}