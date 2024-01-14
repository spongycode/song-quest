package com.spongycode.songquest.screen.gameplay.playing.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ScoreBoard(score: Float) {
    Text(
        text = "Score: ${score.toInt()}",
        fontWeight = FontWeight.W800,
        fontSize = 25.sp,
        color = MaterialTheme.colorScheme.primary
    )
}