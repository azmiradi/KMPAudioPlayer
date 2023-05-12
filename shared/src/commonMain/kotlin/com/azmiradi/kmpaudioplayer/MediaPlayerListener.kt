package com.azmiradi.kmpaudioplayer

interface MediaPlayerListener {
    fun onReady()
    fun onVideoCompleted()
    fun onError()
}
