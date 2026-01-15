package com.spongycode.songquest

import android.content.Context
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

actual object PlatformContext {
    private var appContext: Context? = null

    actual fun setContext(context: Any) {
        appContext = context as? Context
    }

    actual fun getContext(): Any? {
        return appContext
    }
}