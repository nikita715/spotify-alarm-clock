package com.nikstep.alarm2.audio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import java.io.File

class AlarmMediaPlayer(context: Context, file: File) {

    private val mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file)).apply {
        setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    fun start() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.apply {
            stop()
            release()
        }
    }

}