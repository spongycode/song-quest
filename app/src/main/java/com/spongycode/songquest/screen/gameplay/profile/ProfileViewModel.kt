package com.spongycode.songquest.screen.gameplay.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.screen.ui_events.SnackBarEvent
import com.spongycode.songquest.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _username = mutableStateOf("username")
    val username: State<String> = _username

    private val _email = mutableStateOf("email")
    val email: State<String> = _email

    private val _profileState = mutableStateOf<ProfileState>(ProfileState.Idle)
    val profileState: State<ProfileState> = _profileState

    private val _snackBarFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    init {
        populatePersonalDetails()
    }

    fun onEvent(event: ProfileEvent) {
        if (_profileState.value != ProfileState.Success) {
            _profileState.value = ProfileState.Idle
        }
        when (event) {
            is ProfileEvent.EnteredUsername -> _username.value = event.value
            is ProfileEvent.SendUpdateProfile -> updateProfile()
            is ProfileEvent.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            datastoreRepository.storeString(DatastoreRepositoryImpl.accessTokenSession, "")
            datastoreRepository.storeString(DatastoreRepositoryImpl.refreshTokenSession, "")
        }
    }

    private fun updateProfile() {
        val validationError = ValidationHelper.validateUsername(_username.value)

        viewModelScope.launch {
            if (validationError != null) {
                _snackBarFlow.emit(
                    SnackBarEvent(
                        show = true,
                        text = validationError
                    )
                )
                return@launch
            }
            _profileState.value = ProfileState.Checking
            try {
                val accessToken =
                    datastoreRepository.getString(DatastoreRepositoryImpl.accessTokenSession)

                val res = authRepository.updateProfile(
                    UserModel(accessToken = accessToken, username = _username.value)
                )
                if (res?.status == "success") {
                    datastoreRepository.storeString(
                        key = DatastoreRepositoryImpl.usernameSession,
                        value = res.data?.user?.username.toString()
                    )
                    _profileState.value = ProfileState.Success
                } else {
                    _snackBarFlow.emit(
                        SnackBarEvent(
                            show = true,
                            text = res?.message.toString()
                        )
                    )
                    _profileState.value = ProfileState.Error
                }
            } catch (_: Exception) {
                _profileState.value = ProfileState.Error
            }
        }
    }

    private fun populatePersonalDetails() {
        viewModelScope.launch {
            _username.value =
                datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession).toString()
            _email.value =
                datastoreRepository.getString(DatastoreRepositoryImpl.emailSession).toString()
        }
    }
}