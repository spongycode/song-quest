package com.spongycode.songquest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spongycode.songquest.di.AppContainer
import com.spongycode.songquest.ui.navigation.NavContainer
import com.spongycode.songquest.ui.theme.SongQuestTheme
import com.spongycode.songquest.util.Constants.SPLASH_SCREEN

@Composable
fun App() {
    AppContainer.init()
    SongQuestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavContainer(
                startDestination = SPLASH_SCREEN,
                appContainer = AppContainer
            )
        }
    }
}