package com.nikstep.alarm.song

import android.media.MediaPlayer

class Song(val name: String, private val mediaPlayer: MediaPlayer) {

    fun stop() = mediaPlayer.stop()
    fun release() = mediaPlayer.release()

}