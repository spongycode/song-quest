package com.spongycode.songquest.ui.screen.gameplay.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.repository.SettingsRepository
import com.spongycode.songquest.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val settingsRepository: SettingsRepository
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
                username = settingsRepository.getString(SettingsRepositoryImpl.USERNAME_SESSION)
                    .toString(),
                email = settingsRepository.getString(SettingsRepositoryImpl.EMAIL_SESSION)
                    .toString(),
                gamesPlayed = settingsRepository.getString(SettingsRepositoryImpl.GAMES_PLAYED_SESSION)
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