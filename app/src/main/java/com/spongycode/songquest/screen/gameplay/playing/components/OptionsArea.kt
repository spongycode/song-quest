package com.spongycode.songquest.screen.gameplay.playing.components

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
import com.spongycode.songquest.R
import com.spongycode.songquest.screen.gameplay.playing.OptionTapState
import com.spongycode.songquest.screen.gameplay.playing.PlayingViewModel
import com.spongycode.songquest.ui.theme.OptionDarkGreen
import com.spongycode.songquest.ui.theme.OptionDarkRed
import com.spongycode.songquest.ui.theme.OptionDarkYellow
import com.spongycode.songquest.ui.theme.OptionLightGreen
import com.spongycode.songquest.ui.theme.OptionLightRed
import com.spongycode.songquest.ui.theme.OptionLightYellow
import com.spongycode.songquest.util.Constants

@Composable
fun OptionsArea(
    viewModel: PlayingViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(viewModel.questions[viewModel.currentSongIndex.intValue].options) { option ->
            OptionField(
                text = option.value!!,
                onClick = {
                    if ((viewModel.optionTapState.value == OptionTapState.Idle)) {
                        viewModel.tapButton(option.optionid!!)
                        viewModel.checkAnswer(option.optionid)
                    }
                },
                fillColor = if (option.optionid!! != viewModel.tappedButtonId.intValue) Color.White else {
                    when (viewModel.optionTapState.value) {
                        OptionTapState.Checking -> OptionLightYellow
                        OptionTapState.CorrectAnswer -> OptionLightGreen
                        OptionTapState.Idle -> Color.White
                        OptionTapState.WrongAnswer -> OptionLightRed
                    }
                },
                tint = if (option.optionid != viewModel.tappedButtonId.intValue) MaterialTheme.colorScheme.primary else {
                    when (viewModel.optionTapState.value) {
                        OptionTapState.Checking -> OptionDarkYellow
                        OptionTapState.CorrectAnswer -> OptionDarkGreen
                        OptionTapState.Idle -> MaterialTheme.colorScheme.primary
                        OptionTapState.WrongAnswer -> OptionDarkRed
                    }
                },
                iconId = if (option.optionid != viewModel.tappedButtonId.intValue) null else {
                    when (viewModel.optionTapState.value) {
                        OptionTapState.Checking -> null
                        OptionTapState.CorrectAnswer -> R.drawable.baseline_check_circle_24
                        OptionTapState.Idle -> null
                        OptionTapState.WrongAnswer -> R.drawable.baseline_cancel_24
                    }
                }
            )
            Spacer(modifier = Modifier.height(Constants.SMALL_HEIGHT))
        }
    }
}