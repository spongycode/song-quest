package com.spongycode.songquest.screen.gameplay.playing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.gameplay.playing.components.HealthMeter
import com.spongycode.songquest.screen.gameplay.playing.components.OptionsArea
import com.spongycode.songquest.screen.gameplay.playing.components.QuestionTitle
import com.spongycode.songquest.screen.gameplay.playing.components.ScoreBoard
import com.spongycode.songquest.util.Constants

@Composable
fun PlayingScreen(
    category: String,
    navController: NavHostController,
    viewModel: PlayingViewModel = hiltViewModel()
) {
    val createGameState = viewModel.createGameState.value
    val isGameOver = viewModel.isGameOver.value

    LaunchedEffect(category) {
        viewModel.createGame(category)
    }

    if (isGameOver) {
        navController.popBackStack()
        navController.navigate("gameover/${viewModel.game.value._id}")
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

            CreateGameState.Success -> PlayingScreenSuccess(viewModel)
        }
    }
}


@Composable
fun PlayingScreenSuccess(
    viewModel: PlayingViewModel
) {
    if (viewModel.currentSongIndex.intValue < viewModel.questions.size) {
        LaunchedEffect(viewModel.currentSongIndex.intValue) {
            viewModel.playCurrentSong()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 80.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HealthMeter(totalLife = viewModel.totalLife.intValue)
                ScoreBoard(score = viewModel.currentScore.value)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                QuestionTitle(viewModel.questions[viewModel.currentSongIndex.intValue].title.toString())
                OptionsArea(viewModel)
            }
        }
    }
}


@Composable
fun PlayingScreenPlaceholder(
    text: String,
    category: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 200.dp),
            text = text,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.W600
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red),
            painter = painterResource(
                id = when (category) {
                    Constants.BOLLYWOOD_CODE -> {
                        R.drawable.bollywood_banner
                    }

                    Constants.HOLLYWOOD_CODE -> {
                        R.drawable.hollywood_banner
                    }

                    Constants.DESI_HIP_HOP_CODE -> {
                        R.drawable.desi_hip_hop_banner
                    }

                    else -> {
                        R.drawable.hip_hop_banner
                    }
                }
            ), contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
    }
}