package com.spongycode.songquest.ui.screen.gameplay.playing

import android.media.MediaPlayer
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
import com.spongycode.songquest.util.Constants.GAME_OVER_SCREEN
import com.spongycode.songquest.util.Constants.TIME_PER_QUESTION
import com.spongycode.songquest.util.Constants.TOTAL_CHANCE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayingViewModel(
    private val gameplayRepository: GameplayRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayingUiState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<PlayingViewEffect>()
    val viewEffect: SharedFlow<PlayingViewEffect> = _viewEffect.asSharedFlow()

    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    private var timerJob: Job? = null

    fun onEvent(event: PlayingEvent) {
        when (event) {
            is PlayingEvent.CheckAnswer -> checkAnswer(event.optionId)
            is PlayingEvent.CreateGame -> createGame(event.category)
            is PlayingEvent.Navigate -> {
                viewModelScope.launch {
                    _viewEffect.emit(
                        PlayingViewEffect.Navigate(
                            route = event.route,
                            popBackStack = event.popBackStack
                        )
                    )
                }
            }

            is PlayingEvent.TapButton -> tapButton(event.optionId)
            PlayingEvent.PlayCurrentSong -> playCurrentSong()
        }
    }

    private fun createGame(category: String) {
        _uiState.value = _uiState.value.copy(
            currentScore = 0f
        )
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
                    _uiState.value = _uiState.value.copy(
                        game = it
                    )
                }
                _uiState.value = _uiState.value.copy(
                    questions = res.data?.questions ?: emptyList(),
                    createGameState = CreateGameState.Success
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    createGameState = CreateGameState.Error
                )
            }
        }
    }

    private fun checkAnswer(optionId: Int) {
        if (optionId == -1) {
            if (_uiState.value.totalLife == 1) {
                mediaPlayer?.release()
                _uiState.value = _uiState.value.copy(
                    isGameOver = true
                )
                viewModelScope.launch {
                    _viewEffect.emit(PlayingViewEffect.Navigate(route = "$GAME_OVER_SCREEN/${_uiState.value.game._id}"))
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    totalLife = _uiState.value.totalLife - 1
                )
                nextQuestion()
            }
            return
        }
        _uiState.value = _uiState.value.copy(
            optionTapState = Checking
        )
        timerJob?.cancel()
        viewModelScope.launch {
            val res = gameplayRepository.checkAnswer(
                CheckAnswerModel(
                    gameId = _uiState.value.game._id.toString(),
                    questionId = _uiState.value.questions[_uiState.value.currentSongIndex]._id.toString(),
                    optionId = optionId,
                    timeTaken = TIME_PER_QUESTION - _uiState.value.time
                )
            )
            if (res?.status == "success") {
                if (res.data?.isCorrect!!) {
                    _uiState.value = _uiState.value.copy(
                        optionTapState = CorrectAnswer
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        optionTapState = WrongAnswer
                    )
                    if (_uiState.value.totalLife == 1) {
                        mediaPlayer?.release()
                        _uiState.value = _uiState.value.copy(
                            isGameOver = true
                        )
                        viewModelScope.launch {
                            _viewEffect.emit(PlayingViewEffect.Navigate(route = "$GAME_OVER_SCREEN/${_uiState.value.game._id}"))
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            totalLife = _uiState.value.totalLife - 1
                        )
                    }
                }
                _uiState.value = _uiState.value.copy(
                    currentScore = _uiState.value.currentScore + res.data.increment!!
                )
                if (_uiState.value.currentSongIndex == _uiState.value.questions.size - 1) {
                    _uiState.value = _uiState.value.copy(
                        isGameOver = true
                    )
                    viewModelScope.launch {
                        _viewEffect.emit(PlayingViewEffect.Navigate(route = "$GAME_OVER_SCREEN/${_uiState.value.game._id}"))
                    }
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
        _uiState.value = _uiState.value.copy(
            showOptions = false,
            time = TIME_PER_QUESTION,
        )
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            optionTapState = Idle,
            tappedButtonId = -1,
            currentSongIndex = _uiState.value.currentSongIndex + 1
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showOptions = true,
                time = TIME_PER_QUESTION
            )
            while (true) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    time = _uiState.value.time - 1
                )
                if (_uiState.value.time == 0) {
                    checkAnswer(-1)
                }
            }
        }
    }

    private fun playCurrentSong() {
        val index = _uiState.value.currentSongIndex
        if (index < _uiState.value.questions.size) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(_uiState.value.questions[index].songUrl)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
                startTimer()
                mediaPlayer?.isLooping = true
            }
        }
    }

    private fun tapButton(buttonId: Int) {
        _uiState.value = _uiState.value.copy(
            tappedButtonId = buttonId
        )
    }

    override fun onCleared() {
        mediaPlayer?.release()
        super.onCleared()
    }
}

data class PlayingUiState(
    val currentSongIndex: Int = 0,
    val tappedButtonId: Int = -1,
    val totalLife: Int = TOTAL_CHANCE,
    val currentScore: Float = 0f,
    val game: GameModel = GameModel(),
    val optionTapState: OptionTapState = Idle,
    val createGameState: CreateGameState = CreateGameState.Fetching,
    val isGameOver: Boolean = false,
    val showOptions: Boolean = false,
    val questions: List<QuestionModel> = emptyList(),
    val time: Int = TIME_PER_QUESTION
)

sealed interface PlayingViewEffect {
    data class Navigate(val route: String, val popBackStack: Boolean = true) : PlayingViewEffect
}

sealed interface PlayingEvent {
    data class CheckAnswer(val optionId: Int) : PlayingEvent
    data class TapButton(val optionId: Int) : PlayingEvent
    data class Navigate(val route: String, val popBackStack: Boolean = true) : PlayingEvent
    data class CreateGame(val category: String) : PlayingEvent
    data object PlayCurrentSong : PlayingEvent
}