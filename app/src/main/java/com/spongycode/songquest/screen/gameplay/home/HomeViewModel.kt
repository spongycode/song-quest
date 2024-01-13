package com.spongycode.songquest.screen.gameplay.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl
import com.spongycode.songquest.domain.repository.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    init {
        viewModelScope.launch {
            _username.value =
                datastoreRepository.getString(DatastoreRepositoryImpl.usernameSession).toString()

            _email.value =
                datastoreRepository.getString(DatastoreRepositoryImpl.emailSession).toString()
        }
    }
}