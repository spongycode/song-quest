package com.spongycode.songquest

import androidx.compose.ui.graphics.ImageBitmap

expect fun ImageBitmap.share(context: PlatformContext, score: Int?, category: String?)
