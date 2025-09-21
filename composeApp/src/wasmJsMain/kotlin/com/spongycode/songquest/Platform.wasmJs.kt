package com.spongycode.songquest

import kotlinx.browser.window

actual fun platform(): String = "Web"
actual fun getScreenHeight(): Float {
    return window.innerHeight.toFloat()
}

actual fun getScreenWidth(): Float {
    return window.innerWidth.toFloat()
}