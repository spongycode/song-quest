package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.spongycode.songquest.data.model.gameplay.QuestionModel
import com.spongycode.songquest.ui.screen.gameplay.playing.OptionTapState
import com.spongycode.songquest.ui.theme.OptionDarkGreen
import com.spongycode.songquest.ui.theme.OptionDarkRed
import com.spongycode.songquest.ui.theme.OptionDarkYellow
import com.spongycode.songquest.ui.theme.OptionLightGreen
import com.spongycode.songquest.ui.theme.OptionLightRed
import com.spongycode.songquest.ui.theme.OptionLightYellow
import com.spongycode.songquest.util.Constants
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.baseline_cancel_24
import song_quest.composeapp.generated.resources.baseline_check_circle_24

@Composable
fun OptionsArea(
    questions: List<QuestionModel>,
    currentSongIndex: Int,
    tappedButtonId: Int,
    optionTapState: OptionTapState = OptionTapState.Idle,
    onTapButton: (Int) -> Unit = {},
    onCheckAnswer: (Int) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(questions[currentSongIndex].options) { option ->
            OptionField(
                text = option.value!!,
                onClick = {
                    if ((optionTapState == OptionTapState.Idle)) {
                        onTapButton(option.optionid)
                        onCheckAnswer(option.optionid)
                    }
                },
                fillColor = if (option.optionid!! != tappedButtonId) Color.White else {
                    when (optionTapState) {
                        OptionTapState.Checking -> OptionLightYellow
                        OptionTapState.CorrectAnswer -> OptionLightGreen
                        OptionTapState.Idle -> Color.White
                        OptionTapState.WrongAnswer -> OptionLightRed
                    }
                },
                tint = if (option.optionid != tappedButtonId) MaterialTheme.colorScheme.primary else {
                    when (optionTapState) {
                        OptionTapState.Checking -> OptionDarkYellow
                        OptionTapState.CorrectAnswer -> OptionDarkGreen
                        OptionTapState.Idle -> MaterialTheme.colorScheme.primary
                        OptionTapState.WrongAnswer -> OptionDarkRed
                    }
                },
                iconId = if (option.optionid != tappedButtonId) null else {
                    when (optionTapState) {
                        OptionTapState.Checking -> null
                        OptionTapState.CorrectAnswer -> Res.drawable.baseline_check_circle_24
                        OptionTapState.Idle -> null
                        OptionTapState.WrongAnswer -> Res.drawable.baseline_cancel_24
                    }
                }
            )
            Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))
        }
    }
}