package com.spongycode.songquest.screen.auth.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spongycode.songquest.util.Fonts

@Composable
fun TitleText(title: String) {
    Text(
        text = title,
        fontSize = 30.sp,
        fontWeight = FontWeight.W600,
        fontFamily = Fonts.poppinsFamily
    )
}