package com.spongycode.songquest.ui.screen.starter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.refreshTokenSession
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.ui.screen.ui_events.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _navigationFlow = MutableSharedFlow<NavigationEvent>()
    val navigationFlow: SharedFlow<NavigationEvent> = _navigationFlow

    init {
        refreshToken()
    }

    private fun refreshToken() {
        viewModelScope.launch {
            try {
                val refreshToken = datastoreRepository.getString(refreshTokenSession)
                if (refreshToken.isNullOrBlank()) {
                    _navigationFlow.emit(NavigationEvent(route = "register"))
                } else {
                    val res = authRepository.refreshToken(refreshToken)
                    if (res?.status == "success") {
                        datastoreRepository.storeString(
                            key = DatastoreRepositoryImpl.accessTokenSession,
                            value = res.data?.accessToken.toString()
                        )
                        datastoreRepository.storeString(
                            key = refreshTokenSession,
                            value = res.data?.refreshToken.toString()
                        )
                        datastoreRepository.storeString(
                            key = DatastoreRepositoryImpl.usernameSession,
                            value = res.data?.user?.username.toString()
                        )
                        datastoreRepository.storeString(
                            key = DatastoreRepositoryImpl.emailSession,
                            value = res.data?.user?.email.toString()
                        )
                        datastoreRepository.storeString(
                            key = DatastoreRepositoryImpl.gamesPlayedSession,
                            value = res.data?.user?.gamesPlayed.toString()
                        )
                        _navigationFlow.emit(NavigationEvent(route = "home"))
                    } else {
                        _navigationFlow.emit(NavigationEvent(route = "register"))
                    }
                }
            } catch (_: Exception) {
                _navigationFlow.emit(NavigationEvent(route = "register"))
            }
        }
    }
}