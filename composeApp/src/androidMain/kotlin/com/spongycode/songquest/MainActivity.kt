package com.spongycode.songquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SongQuest)
        super.onCreate(savedInstanceState)


        setContent {
            App()
//            SongQuestTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    NavContainer(
//                        startDestination = SPLASH_SCREEN,
//                        container = AppContainer
//                    )
//                }
//            }
        }
    }
}