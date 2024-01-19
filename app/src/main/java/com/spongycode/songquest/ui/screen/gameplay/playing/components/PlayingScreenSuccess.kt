package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spongycode.songquest.ui.screen.gameplay.playing.PlayingViewModel
import com.spongycode.songquest.util.Constants

@Composable
fun PlayingScreenSuccess(
    viewModel: PlayingViewModel = hiltViewModel()
) {
    val transitionData = updateCircularTransitionData(
        remainingTime = viewModel.time.intValue.toLong(),
        totalTime = Constants.TIME_PER_QUESTION.toLong()
    )
    if (viewModel.currentSongIndex.intValue < viewModel.questions.size) {
        LaunchedEffect(viewModel.currentSongIndex.intValue) {
            viewModel.playCurrentSong()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 60.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HealthMeter(totalLife = viewModel.totalLife.intValue)
                ScoreBoard(score = viewModel.currentScore.value)
            }

            CircularTimer(transitionData = transitionData, time = viewModel.time.intValue)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                QuestionTitle(viewModel.questions[viewModel.currentSongIndex.intValue].title.toString())
                if (viewModel.showOptions.value) {
                    OptionsArea()
                }
            }
        }
    }
}

