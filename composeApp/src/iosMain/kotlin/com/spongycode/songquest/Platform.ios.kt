package com.spongycode.songquest

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.UIKit.UIScreen

actual fun platform(): String = "iOS"
@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidth(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetWidth(bounds).toFloat()
}

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetHeight(bounds).toFloat()
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