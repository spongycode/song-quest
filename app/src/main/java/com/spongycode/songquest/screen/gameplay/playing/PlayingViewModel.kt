package com.spongycode.songquest.screen.gameplay.playing

import android.media.MediaPlayer
import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spongycode.songquest.data.model.gameplay.CheckAnswerModel
import com.spongycode.songquest.data.model.gameplay.CreateGameModel
import com.spongycode.songquest.data.model.gameplay.GameModel
import com.spongycode.songquest.data.model.gameplay.QuestionModel
import com.spongycode.songquest.data.repository.DatastoreRepositoryImpl.Companion.accessTokenSession
import com.spongycode.songquest.domain.repository.DatastoreRepository
import com.spongycode.songquest.domain.repository.GameplayRepository
import com.spongycode.songquest.screen.gameplay.playing.PlayingState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _tappedButtonId = mutableIntStateOf(-1)
    val tappedButtonId: IntState = _tappedButtonId

    private val _isCorrect = mutableStateOf(false)
    val isCorrect: State<Boolean> = _isCorrect

    private val _game = mutableStateOf(
        GameModel(
            null, null, emptyList(), null, null
        )
    )

    private val _playingState = mutableStateOf<PlayingState>(Idle)
    val playingState: State<PlayingState> = _playingState

    private val _questions = mutableStateListOf<QuestionModel>()
    val questions: SnapshotStateList<QuestionModel> = _questions
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    fun createGame(category: String) {
        _category.value = category
        viewModelScope.launch {
            val accessToken = datastoreRepository.getString(accessTokenSession)
            val res = gameplayRepository.createGame(
                CreateGameModel(
                    accessToken = accessToken!!,
                    count = 100,
                    category = category
                )
            )

            if (res?.status == "success") {

                res.data?.game?.let {
                    _game.value = it
                }

                _questions.clear()
                _questions.addAll(res.data?.questions ?: emptyList())
            }
        }
    }

    fun checkAnswer(optionId: Int) {
        _playingState.value = Checking
        viewModelScope.launch {
            val res = gameplayRepository.checkAnswer(
                CheckAnswerModel(
                    gameId = _game.value._id.toString(),
                    questionId = _questions[currentSongIndex.intValue]._id.toString(),
                    optionId = optionId,
                    timeTaken = 20
                )
            )
            if (res?.status == "success") {
                _isCorrect.value = res.data?.isCorrect!!
                if (res.data.isCorrect) {
                    _playingState.value = CorrectAnswer
                } else {
                    _playingState.value = WrongAnswer
                }

                delay(2000)

                nextQuestion()
            } else {
                return@launch
            }
        }
    }

    private fun nextQuestion() {
        _playingState.value = Idle
        _isCorrect.value = false
        _tappedButtonId.intValue = -1
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
                mediaPlayer?.isLooping = true
//                mediaPlayer?.setOnCompletionListener {
//                    increase()
//                }
            }
        }
    }

    fun tapButton(buttonId: Int) {
        _tappedButtonId.intValue = buttonId
    }

    override fun onCleared() {
        mediaPlayer?.release()
        super.onCleared()
    }
}