package com.spongycode.songquest

import android.media.MediaPlayer

actual class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var sourceUrl: String? = null

    actual fun setSource(url: String) {
        sourceUrl = url
    }

    actual fun prepareAndPlay(looping: Boolean) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(sourceUrl)
            isLooping = looping
            setOnPreparedListener { start() }
            prepareAsync()
        }
    }

    actual fun stop() {
        mediaPlayer?.stop()
    }

    actual fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
