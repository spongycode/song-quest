package com.spongycode.songquest.ui.screen.gameplay.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.auth.UserModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.repository.AuthRepository
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.util.ValidationHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<ProfileViewEffect>()
    val viewEffect: SharedFlow<ProfileViewEffect> = _viewEffect.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        if (_uiState.value.profileState != ProfileState.Success) {
            _uiState.value = _uiState.value.copy(
                profileState = ProfileState.Idle
            )
        }
        when (event) {
            is ProfileEvent.EnteredUsername -> _uiState.value = _uiState.value.copy(
                username = event.value
            )

            is ProfileEvent.SendUpdateProfile -> updateProfile()
            is ProfileEvent.Logout -> logout()
            is ProfileEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(
                        ProfileViewEffect.Navigate(
                            route = event.route,
                            navigateUp = event.navigateUp,
                            popBackStack = event.popBackStack
                        )
                    )
                }
            }

            ProfileEvent.GetPersonalDetails -> getPersonalDetails()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            datastoreRepository.storeString(DatastoreRepositoryImpl.accessTokenSession, "")
            datastoreRepository.storeString(DatastoreRepositoryImpl.refreshTokenSession, "")
        }
    }

    private fun updateProfile() {
        val validationError = ValidationHelper.validateUsername(_uiState.value.username)

        viewModelScope.launch {
            if (validationError != null) {
                _viewEffect.emit(ProfileViewEffect.ShowSnackBar(message = validationError))
                return@launch
            }
            _uiState.value = _uiState.value.copy(
                profileState = ProfileState.Checking
            )
            try {
                val accessToken =
                    datastoreRepository.getString(DatastoreRepositoryImpl.accessTokenSession)

                val res = authRepository.updateProfile(
                    UserModel(accessToken = accessToken, username = _uiState.value.username)
                )
                if (res?.status == "success") {
                    datastoreRepository.storeString(
                        key = DatastoreRepositoryImpl.usernameSession,
                        value = res.data?.user?.username.toString()
                    )
                    _uiState.value = _uiState.value.copy(
                        profileState = ProfileState.Success
                    )
                } else {
                    _viewEffect.emit(ProfileViewEffect.ShowSnackBar(message = res?.message.toString()))
                    _uiState.value = _uiState.value.copy(
                        profileState = ProfileState.Error
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    profileState = ProfileState.Error
                )
            }
        }
    }

    private fun getPersonalDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                username = datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession)
                    .toString(),
                email = datastoreRepository.getString(DatastoreRepositoryImpl.emailSession)
                    .toString()
            )
        }
    }
}

data class ProfileUiState(
    val username: String = "username",
    val email: String = "email",
    val profileState: ProfileState = ProfileState.Idle
)

sealed interface ProfileEvent {
    data class EnteredUsername(val value: String) : ProfileEvent
    data object SendUpdateProfile : ProfileEvent
    data object Logout : ProfileEvent
    data object GetPersonalDetails : ProfileEvent
    data class Navigate(
        val route: String? = null,
        val navigateUp: Boolean = false,
        val popBackStack: Boolean = true
    ) : ProfileEvent
}

sealed interface ProfileViewEffect {
    data class ShowSnackBar(val message: String) : ProfileViewEffect
    data class Navigate(
        val route: String?,
        val navigateUp: Boolean,
        val popBackStack: Boolean
    ) : ProfileViewEffect
}

sealed interface ProfileState {
    data object Idle : ProfileState
    data object Checking : ProfileState
    data object Success : ProfileState
    data object Error : ProfileState
}