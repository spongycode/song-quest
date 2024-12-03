package com.spongycode.songquest.ui.screen.starter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.accessTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.emailSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.gamesPlayedSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.refreshTokenSession
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.usernameSession
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.util.Constants.HOME_SCREEN
import com.spongycode.songquest.util.Constants.REGISTER_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _viewEffect = MutableSharedFlow<SplashViewEffect>()
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
                val refreshToken = datastoreRepository.getString(refreshTokenSession)
                if (refreshToken.isNullOrBlank()) {
                    _viewEffect.emit(SplashViewEffect.Navigate(route = REGISTER_SCREEN))
                } else {
                    val res = authRepository.refreshToken(refreshToken)
                    if (res?.status == "success") {
                        datastoreRepository.storeListString(
                            listOf(
                                Pair(
                                    accessTokenSession,
                                    res.data?.accessToken.toString()
                                ),
                                Pair(
                                    refreshTokenSession,
                                    res.data?.refreshToken.toString()
                                ),
                                Pair(
                                    usernameSession,
                                    res.data?.user?.username.toString()
                                ),
                                Pair(
                                    emailSession,
                                    res.data?.user?.email.toString()
                                ),
                                Pair(
                                    gamesPlayedSession,
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