package com.spongycode.songquest.ui.screen.gameplay.playing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.R
import com.spongycode.songquest.ui.navigation.LocalNavController
import com.spongycode.songquest.ui.screen.gameplay.playing.components.PlayingScreenPlaceholder
import com.spongycode.songquest.ui.screen.gameplay.playing.components.PlayingScreenSuccess

@Composable
fun PlayingScreen(
    category: String,
    viewModel: PlayingViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val createGameState = viewModel.createGameState.value
    val isGameOver = viewModel.isGameOver.value

    LaunchedEffect(category) {
        viewModel.createGame(category)
    }

    if (isGameOver) {
        navController.popBackStack()
        navController.navigate("gameover/${viewModel.game.value._id}")
    }

    if (viewModel.time.intValue == 0) {
        viewModel.checkAnswer(-1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        when (createGameState) {
            CreateGameState.Error -> PlayingScreenPlaceholder(
                stringResource(R.string.error_fetching_question),
                category
            )

            CreateGameState.Fetching -> PlayingScreenPlaceholder(
                stringResource(R.string.loading_questions),
                category
            )

            CreateGameState.Success -> PlayingScreenSuccess()
        }
    }
}