package com.spongycode.songquest.ui.screen.gameplay.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect: SharedFlow<HomeViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(HomeViewEffect.Navigate(event.route, event.popBackStack))
                }
            }

            HomeEvent.GetData -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                username = datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession)
                    .toString(),
                email = datastoreRepository.getString(DatastoreRepositoryImpl.emailSession)
                    .toString(),
                gamesPlayed = datastoreRepository.getString(DatastoreRepositoryImpl.gamesPlayedSession)
                    .toString().toInt()
            )
        }
    }
}

sealed interface HomeEvent {
    data object GetData : HomeEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : HomeEvent
}

data class HomeUiState(
    val username: String = "",
    val email: String = "",
    val gamesPlayed: Int = 0
)

sealed interface HomeViewEffect {
    data class Navigate(val route: String, val popBackStack: Boolean = true) : HomeViewEffect
}