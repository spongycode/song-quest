package com.spongycode.songquest

import android.content.res.Resources

actual fun platform(): String = "Android"
actual fun getScreenWidth(): Float {
    val configuration = Resources.getSystem().configuration
    return configuration.screenWidthDp.toFloat()
}

actual fun getScreenHeight(): Float {
    val configuration = Resources.getSystem().configuration
    return configuration.screenHeightDp.toFloat()
}