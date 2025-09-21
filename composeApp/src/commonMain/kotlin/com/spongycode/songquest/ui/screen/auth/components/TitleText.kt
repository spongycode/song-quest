package com.spongycode.songquest.ui.screen.auth.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.defaultFontFamily

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        fontSize = 30.sp,
        fontWeight = FontWeight.W600,
        fontFamily = defaultFontFamily,
        color = Color.Black
    )
}