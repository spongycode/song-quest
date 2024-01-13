package com.spongycode.songquest.screen.gameplay.playing

import android.media.MediaPlayer
import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.QuestionModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.accessTokenSession
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayingViewModel @Inject constructor(
    private val gameplayRepository: GameplayRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    private val _category = mutableStateOf("")

    private val _currentSongIndex = mutableIntStateOf(0)
    val currentSongIndex: IntState = _currentSongIndex

    private val _questions = mutableStateListOf<QuestionModel>()
    val questions: SnapshotStateList<QuestionModel> = _questions
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    fun createGame(category: String) {
        _category.value = category
        viewModelScope.launch {
            val accessToken = datastoreRepository.getToken(accessTokenSession)
            val res = gameplayRepository.createGame(
                CreateGameModel(
                    accessToken = accessToken!!,
                    count = 100,
                    category = category
                )
            )

            if (res?.status == "success") {
                _questions.clear()
                _questions.addAll(res.data?.questions ?: emptyList())
            }
        }
    }

    private fun increase() {
        _currentSongIndex.intValue++
    }

    fun playCurrentSong() {
        val index = currentSongIndex.intValue
        if (index < _questions.size) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(_questions[index].songUrl)
            mediaPlayer?.prepare()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
                mediaPlayer?.setOnCompletionListener {
                    increase()
                }
            }
        }
    }

    override fun onCleared() {
        mediaPlayer?.release()
        super.onCleared()
    }
}