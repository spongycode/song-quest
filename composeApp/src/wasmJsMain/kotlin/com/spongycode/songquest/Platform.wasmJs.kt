package com.spongycode.songquest

import kotlinx.browser.window

actual fun platform(): String = "Web"
actual fun getScreenHeight(): Float {
    return window.innerHeight.toFloat()
}

actual fun getScreenWidth(): Float {
    return window.innerWidth.toFloat()
}

actual object PlatformContext {
    private var appContext: Any? = null

    actual fun setContext(context: Any) {
        appContext = context
    }

    actual fun getContext(): Any? {
        return appContext
    }
}