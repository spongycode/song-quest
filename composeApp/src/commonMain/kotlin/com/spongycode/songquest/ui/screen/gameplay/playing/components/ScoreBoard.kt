package com.spongycode.songquest.ui.screen.gameplay.playing.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.defaultFontFamily

@Composable
fun ScoreBoard(score: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Score: ",
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = defaultFontFamily
        )
        Text(
            text = "${score.toInt()}",
            fontWeight = FontWeight.W800,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = defaultFontFamily
        )
    }
}
