package com.spongycode.songquest

expect class AudioPlayer() {
    fun setSource(url: String)
    fun prepareAndPlay(looping: Boolean = true)
    fun stop()
    fun release()
}