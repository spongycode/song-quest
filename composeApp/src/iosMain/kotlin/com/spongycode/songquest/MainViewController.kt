package com.spongycode.songquest

import androidx.compose.material3.Text
import androidx.compose.ui.window.ComposeUIViewController


fun MainViewController() = ComposeUIViewController {
    Text("hello ${platform()}")
}