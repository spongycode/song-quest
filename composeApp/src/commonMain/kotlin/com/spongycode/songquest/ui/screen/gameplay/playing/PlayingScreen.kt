package com.spongycode.songquest.ui.screen.gameplay.playing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.playing.components.PlayingScreenPlaceholder
import com.spongycode.songquest.ui.screen.gameplay.playing.components.PlayingScreenSuccess
import com.spongycode.songquest.util.ComposeLocalWrapper
import com.spongycode.songquest.util.Constants.CATEGORY
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.error_fetching_question
import song_quest.composeapp.generated.resources.loading_questions

@Composable
fun PlayingScreenRoot(
    category: String,
    viewModel: PlayingViewModel
) {
    val navController = LocalNavController.current
    LaunchedEffect(null) {
        viewModel.viewEffect.collectLatest {
            when (it) {
                is PlayingViewEffect.Navigate -> {
                    if (it.popBackStack) {
                        navController.popBackStack()
                    }
                    navController.navigate(it.route)
                }
            }
        }
    }
    PlayingScreen(
        uiState = viewModel.uiState.collectAsState().value,
        category = category,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun PlayingScreen(
    modifier: Modifier = Modifier,
    category: String = CATEGORY,
    uiState: PlayingUiState = PlayingUiState(),
    onEvent: (PlayingEvent) -> Unit = {},
) {
    LaunchedEffect(null) {
        onEvent(PlayingEvent.CreateGame(category))
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        when (uiState.createGameState) {
            CreateGameState.Error -> PlayingScreenPlaceholder(
                stringResource(Res.string.error_fetching_question),
                category
            )

            CreateGameState.Fetching -> PlayingScreenPlaceholder(
                stringResource(Res.string.loading_questions),
                category
            )

            CreateGameState.Success -> PlayingScreenSuccess(
                time = uiState.time,
                currentSongIndex = uiState.currentSongIndex,
                questions = uiState.questions,
                totalLife = uiState.totalLife,
                currentScore = uiState.currentScore,
                showOptions = uiState.showOptions,
                tappedButtonId = uiState.tappedButtonId,
                optionTapState = uiState.optionTapState,
                onTapButton = { optionId ->
                    onEvent(PlayingEvent.TapButton(optionId))
                },
                onCheckAnswer = { optionId ->
                    onEvent(PlayingEvent.CheckAnswer(optionId))
                },
                onPlayCurrentSong = {
                    onEvent(PlayingEvent.PlayCurrentSong)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPlayingScreen() {
    ComposeLocalWrapper {
        PlayingScreen()
    }
}