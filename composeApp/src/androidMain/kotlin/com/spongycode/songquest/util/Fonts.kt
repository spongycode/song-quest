package com.spongycode.songquest.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.spongycode.songquest.R

object Fonts {
    val poppinsFamily = FontFamily(
        Font(R.font.poppins_extra_bold, FontWeight.W800),
        Font(R.font.poppins_bold, FontWeight.W600),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_regular, FontWeight.Normal)
    )
}
