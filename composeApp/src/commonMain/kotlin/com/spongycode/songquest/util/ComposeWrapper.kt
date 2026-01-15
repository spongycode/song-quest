package com.spongycode.songquest.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.spongycode.songquest.ui.navigation.LocalNavController

@Composable
fun ComposeLocalWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController(),
        content = content
    )
}