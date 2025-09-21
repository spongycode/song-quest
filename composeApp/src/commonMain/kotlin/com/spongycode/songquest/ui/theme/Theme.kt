package com.spongycode.songquest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    primaryContainer = Color(0xFFF1ECEC),
    secondaryContainer = Color(0xFFE2D8D8),
    inversePrimary = Color(0xFF777373),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White,
)

@Composable
fun SongQuestTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}