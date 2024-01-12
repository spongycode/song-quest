package com.spongycode.songquest.screen.starter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.refreshTokenSession
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.screen.ui_events.NavigationEvent
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
                val refreshToken = datastoreRepository.getToken(refreshTokenSession)
                if (refreshToken.isNullOrBlank()) {
                    _navigationFlow.emit(NavigationEvent(route = "register"))
                } else {
                    val res = authRepository.refreshToken(refreshToken)
                    if (res?.status == "success") {
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