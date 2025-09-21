package com.spongycode.songquest

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.currentItem
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.kCMTimeZero
import platform.Foundation.NSURL

actual class AudioPlayer actual constructor() {
    private var player: AVPlayer? = null
    private var sourceUrl: String? = null

    actual fun setSource(url: String) {
        sourceUrl = url
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun prepareAndPlay(looping: Boolean) {
        val nsUrl = NSURL(string = sourceUrl ?: return)
        player = AVPlayer.playerWithURL(nsUrl)
        player?.play()

        if (looping) {
            // Add observer to loop playback
            platform.Foundation.NSNotificationCenter.defaultCenter.addObserverForName(
                name = AVPlayerItemDidPlayToEndTimeNotification,
                `object` = player?.currentItem,
                queue = null
            ) { _ ->
                player?.seekToTime(cValue { value = 0L })
                player?.play()
            }
        }
    }

    actual fun stop() {
        player?.pause()
    }

    actual fun release() {
        player?.pause()
        player = null
    }
}

