package com.azmiradi.kmpaudioplayer

expect class MediaPlayerController {
    fun prepare(pathSource: String, listener: MediaPlayerListener)

    fun start()

    fun pause()

    fun stop()

    fun seekTo(float: Float)

    fun isPlaying(): Boolean

    fun release()
}
