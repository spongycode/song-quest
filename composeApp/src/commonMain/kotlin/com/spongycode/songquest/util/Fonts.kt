package com.spongycode.songquest.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.poppins_bold
import song_quest.composeapp.generated.resources.poppins_extra_bold
import song_quest.composeapp.generated.resources.poppins_medium
import song_quest.composeapp.generated.resources.poppins_regular

object Fonts {
    @Composable
    fun poppinsFamily(): FontFamily {
        return FontFamily.Default
//        return FontFamily(
//            Font(Res.font.poppins_extra_bold, FontWeight.W800),
//            Font(Res.font.poppins_bold, FontWeight.W600),
//            Font(Res.font.poppins_medium, FontWeight.Medium),
//            Font(Res.font.poppins_regular, FontWeight.Normal)
//        )
    }
}