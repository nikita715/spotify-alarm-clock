package com.nikstep.alarm2

import android.media.MediaPlayer

private var activePlayer: MediaPlayer? = null

@Synchronized
fun setActiveMediaPlayer(mediaPlayer: MediaPlayer) {
    stopActiveMediaPlayer()
    activePlayer = mediaPlayer
}

@Synchronized
fun stopActiveMediaPlayer() {
    activePlayer?.apply {
        stop()
        release()
    }
    activePlayer = null
}