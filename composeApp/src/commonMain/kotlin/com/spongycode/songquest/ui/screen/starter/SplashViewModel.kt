package com.spongycode.songquest.ui.screen.starter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.repository.AuthRepository
import com.spongycode.songquest.repository.SettingsRepository
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.ACCESS_TOKEN_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.EMAIL_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.GAMES_PLAYED_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.REFRESH_TOKEN_SESSION
import com.spongycode.songquest.repository.SettingsRepositoryImpl.Companion.USERNAME_SESSION
import com.spongycode.songquest.util.Constants.HOME_SCREEN
import com.spongycode.songquest.util.Constants.REGISTER_SCREEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _viewEffect = MutableSharedFlow<SplashViewEffect>(replay = 1)
    val viewEffect: SharedFlow<SplashViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.RefreshToken -> {
                refreshToken()
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            try {
                val refreshToken = settingsRepository.getString(REFRESH_TOKEN_SESSION)
                if (refreshToken.isNullOrBlank()) {
                    _viewEffect.emit(SplashViewEffect.Navigate(route = REGISTER_SCREEN))
                } else {
                    val res = authRepository.refreshToken(refreshToken)
                    if (res?.status == "success") {
                        settingsRepository.storeListString(
                            listOf(
                                Pair(
                                    ACCESS_TOKEN_SESSION,
                                    res.data?.accessToken.toString()
                                ),
                                Pair(
                                    REFRESH_TOKEN_SESSION,
                                    res.data?.refreshToken.toString()
                                ),
                                Pair(
                                    USERNAME_SESSION,
                                    res.data?.user?.username.toString()
                                ),
                                Pair(
                                    EMAIL_SESSION,
                                    res.data?.user?.email.toString()
                                ),
                                Pair(
                                    GAMES_PLAYED_SESSION,
                                    res.data?.user?.gamesPlayed.toString()
                                )
                            )
                        )
                        _viewEffect.emit(SplashViewEffect.Navigate(route = HOME_SCREEN))
                    } else {
                        _viewEffect.emit(SplashViewEffect.Navigate(route = REGISTER_SCREEN))
                    }
                }
            } catch (_: Exception) {
                _viewEffect.emit(SplashViewEffect.Navigate(route = REGISTER_SCREEN))
            }
        }
    }
}


sealed interface SplashEvent {
    data object RefreshToken : SplashEvent
}

sealed interface SplashViewEffect {
    data class Navigate(val route: String, val popBackStack: Boolean = true) : SplashViewEffect
}