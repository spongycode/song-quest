package com.spongycode.songquest

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement

actual class AudioPlayer actual constructor() {
    private var audio: HTMLAudioElement? = null
    private var sourceUrl: String? = null

    actual fun setSource(url: String) {
        sourceUrl = url
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun prepareAndPlay(looping: Boolean) {
        audio = document.createElement("audio") as HTMLAudioElement
        audio?.src = sourceUrl ?: return
        audio?.loop = looping
        audio?.play()
    }

    actual fun stop() {
        audio?.pause()
    }

    actual fun release() {
        audio?.pause()
        audio = null
    }
}