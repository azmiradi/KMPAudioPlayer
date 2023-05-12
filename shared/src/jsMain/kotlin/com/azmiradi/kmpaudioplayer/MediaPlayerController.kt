package com.azmiradi.kmpaudioplayer

import kotlinx.browser.document
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.events.EventListener

actual class MediaPlayerController {
    private val audioElement = document.createElement("audio") as HTMLAudioElement

    actual fun prepare(
        pathSource: String,
        listener: MediaPlayerListener
    ) {
        audioElement.src = pathSource
        audioElement.addEventListener("canplaythrough", {
            // Audio is ready to play without interruption
            listener.onReady()
        })

        audioElement.onended = {
            listener.onVideoCompleted()
        }
        audioElement.addEventListener("error", {
            listener.onError()
        })
    }

    actual fun start() {
        audioElement.play()
    }

    actual fun pause() {
        audioElement.pause()
    }

    actual fun stop() {
        audioElement.pause()
    }

    actual fun isPlaying(): Boolean {
        return !audioElement.paused
    }

    actual fun release() {
        audioElement.src = ""
    }

    actual fun seekTo(float: Float) {
        audioElement.currentTime = float.toDouble()
    }
}
