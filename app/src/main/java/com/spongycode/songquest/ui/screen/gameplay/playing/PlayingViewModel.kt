package com.spongycode.songquest.ui.screen.gameplay.playing

import android.media.MediaPlayer
import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
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
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState.Checking
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState.CorrectAnswer
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState.Idle
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState.WrongAnswer
import com.spongycode.songquest.util.Constants.TIME_PER_QUESTION
import com.spongycode.songquest.util.Constants.TOTAL_CHANCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private val _totalLife = mutableIntStateOf(TOTAL_CHANCE)
    val totalLife: IntState = _totalLife

    private val _isCorrect = mutableStateOf(false)

    private val _currentScore = mutableFloatStateOf(0f)
    val currentScore: State<Float> = _currentScore

    private val _game = mutableStateOf(GameModel(null, null, null, emptyList(), null, null))
    val game: State<GameModel> = _game

    private val _optionTapState = mutableStateOf<OptionTapState>(Idle)
    val optionTapState: State<OptionTapState> = _optionTapState

    private val _createGameState = mutableStateOf<CreateGameState>(CreateGameState.Fetching)
    val createGameState: State<CreateGameState> = _createGameState

    private val _isGameOver = mutableStateOf(false)
    val isGameOver: State<Boolean> = _isGameOver

    private val _showOptions = mutableStateOf(false)
    val showOptions: State<Boolean> = _showOptions

    private val _questions = mutableStateListOf<QuestionModel>()
    val questions: SnapshotStateList<QuestionModel> = _questions

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    private val _time = mutableIntStateOf(TIME_PER_QUESTION)
    val time: IntState = _time

    init {
        mediaPlayer = MediaPlayer()
    }

    fun createGame(category: String) {
        _category.value = category
        _currentScore.floatValue = 0f
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
                _createGameState.value = CreateGameState.Success
            } else {
                _createGameState.value = CreateGameState.Error
            }
        }
    }

    fun checkAnswer(optionId: Int) {
        if (optionId == -1) {
            if (_totalLife.intValue == 1) {
                mediaPlayer?.release()
                _isGameOver.value = true
            } else {
                _totalLife.intValue--
                nextQuestion()
            }
            return
        }
        _optionTapState.value = Checking
        timerJob?.cancel()
        viewModelScope.launch {
            val res = gameplayRepository.checkAnswer(
                CheckAnswerModel(
                    gameId = _game.value._id.toString(),
                    questionId = _questions[currentSongIndex.intValue]._id.toString(),
                    optionId = optionId,
                    timeTaken = TIME_PER_QUESTION - _time.intValue
                )
            )
            if (res?.status == "success") {
                _isCorrect.value = res.data?.isCorrect!!
                if (res.data.isCorrect) {
                    _optionTapState.value = CorrectAnswer
                } else {
                    _optionTapState.value = WrongAnswer
                    if (_totalLife.intValue == 1) {
                        mediaPlayer?.release()
                        _isGameOver.value = true
                    } else {
                        _totalLife.intValue--
                    }
                }

                _currentScore.floatValue += res.data.increment!!
                if (_currentSongIndex.intValue == _questions.size - 1) {
                    _isGameOver.value = true
                } else {
                    delay(500)
                    nextQuestion()
                }
            } else {
                return@launch
            }
        }
    }

    private fun nextQuestion() {
        _showOptions.value = false
        _time.intValue = TIME_PER_QUESTION
        timerJob?.cancel()
        _optionTapState.value = Idle
        _tappedButtonId.intValue = -1
        _currentSongIndex.intValue++
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _showOptions.value = true
            _time.intValue = TIME_PER_QUESTION
            while (true) {
                delay(1000)
                _time.intValue--
            }
        }
    }

    fun playCurrentSong() {
        val index = currentSongIndex.intValue
        if (index < _questions.size) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(_questions[index].songUrl)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
                startTimer()
                mediaPlayer?.isLooping = true
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