package com.spongycode.songquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spongycode.songquest.ui.navigation.NavContainer
import com.spongycode.songquest.ui.theme.SongQuestTheme
import com.spongycode.songquest.util.Constants.SPLASH_SCREEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SongQuest)
        super.onCreate(savedInstanceState)
        setContent {
            SongQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavContainer(startDestination = SPLASH_SCREEN)
                }
            }
        }
    }
}