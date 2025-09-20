package com.spongycode.songquest

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.jetbrains.compose.resources.painterResource
import song_quest.composeapp.generated.resources.Res
import song_quest.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        Text("hello ${platform()}")
    }
}