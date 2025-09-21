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
import com.spongycode.songquest.data.model.gameplay.QuestionModel
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState
import com.spongycode.songquest.util.Constants

@Composable
fun PlayingScreenSuccess(
    time: Int,
    currentSongIndex: Int,
    questions: List<QuestionModel>,
    totalLife: Int,
    currentScore: Float,
    showOptions: Boolean,
    tappedButtonId: Int,
    optionTapState: OptionTapState = OptionTapState.Idle,
    onTapButton: (Int) -> Unit = {},
    onCheckAnswer: (Int) -> Unit = {},
    onPlayCurrentSong: () -> Unit
) {
    val transitionData = updateCircularTransitionData(
        remainingTime = time.toLong(),
        totalTime = Constants.TIME_PER_QUESTION.toLong()
    )
    if (currentSongIndex < questions.size) {
        LaunchedEffect(currentSongIndex) {
            onPlayCurrentSong()
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
                HealthMeter(totalLife = totalLife)
                ScoreBoard(score = currentScore)
            }

            CircularTimer(transitionData = transitionData, time = time)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                QuestionTitle(questions[currentSongIndex].title.toString())
                if (showOptions) {
                    OptionsArea(
                        questions = questions,
                        currentSongIndex = currentSongIndex,
                        tappedButtonId = tappedButtonId,
                        optionTapState = optionTapState,
                        onTapButton = { optionId ->
                            onTapButton(optionId)
                        },
                        onCheckAnswer = { optionId ->
                            onCheckAnswer(optionId)
                        }
                    )
                }
            }
        }
    }
}