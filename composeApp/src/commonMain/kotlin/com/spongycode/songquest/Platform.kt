package com.spongycode.songquest

expect fun platform(): String
expect fun getScreenWidth(): Float
expect fun getScreenHeight(): Float

expect object PlatformContext {
    fun setContext(context: Any)
    fun getContext(): Any?
}