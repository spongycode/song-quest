package com.spongycode.songquest.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.poppins_bold
import song_quest.composeapp.generated.resources.poppins_extra_bold
import song_quest.composeapp.generated.resources.poppins_light
import song_quest.composeapp.generated.resources.poppins_medium
import song_quest.composeapp.generated.resources.poppins_regular

val defaultFontFamily: FontFamily
    @Composable get() = FontFamily.Default
//    @Composable get() = FontFamily(
//        Font(weight = FontWeight.Bold, resource = Res.font.poppins_bold),
//        Font(weight = FontWeight.Medium, resource = Res.font.poppins_medium),
//        Font(weight = FontWeight.Normal, resource = Res.font.poppins_regular),
//        Font(weight = FontWeight.SemiBold, resource = Res.font.poppins_light),
//        Font(weight = FontWeight.ExtraBold, resource = Res.font.poppins_extra_bold)
//    )