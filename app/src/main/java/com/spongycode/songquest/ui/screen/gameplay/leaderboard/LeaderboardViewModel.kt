package com.spongycode.songquest.ui.screen.gameplay.leaderboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.AuthModel
import com.spongycode.songquest.data.model.gameplay.LeaderboardUsersModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import com.spongycode.songquest.util.CategoryConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val gameplayRepository: GameplayRepository
) : ViewModel() {

    private val _leaderboardState = mutableStateOf<LeaderboardState>(LeaderboardState.Loading)
    val leaderboardState: State<LeaderboardState> = _leaderboardState

    private val _leaderboardDatabase = mutableStateMapOf<String, List<LeaderboardUsersModel>>()
    val leaderboardDatabase: SnapshotStateMap<String, List<LeaderboardUsersModel>> =
        _leaderboardDatabase

    private val _selectedCategory = mutableStateOf(CategoryConvertor.giveAllCategories()[0])
    val selectedCategory: State<String> = _selectedCategory

    init {
        fetchLeaderboard()
    }

    fun changeSelectedCategory(category: String) {
        _selectedCategory.value = category
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
                        _leaderboardDatabase[item.category] = item.users
                    }
                    _leaderboardState.value = LeaderboardState.Success
                } else {
                    _leaderboardState.value = LeaderboardState.Error
                }
            } catch (err: Exception) {
                _leaderboardState.value = LeaderboardState.Error
            }
        }
    }
}