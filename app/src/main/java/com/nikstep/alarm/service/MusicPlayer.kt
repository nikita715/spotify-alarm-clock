package com.nikstep.alarm.service

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.nikstep.alarm.cIncreaseVolumeStepSeconds
import com.nikstep.alarm.cMaxMusicLevel
import com.nikstep.alarm.musicFilesPath
import java.io.File

class MusicPlayer(
    private val context: Context,
    private val audioManager: MyAudioManager,
    private val musicProperties: MusicProperties
) {

    private var mediaPlayer: MediaPlayer? = null

    fun playNextSong() {
        Log.i("AlarmReceiver", "Received an alarm trigger")

        val musicFiles = getMusicFiles(context)

        val index = musicProperties.getSongIndex()
        musicProperties.setSongIndex(getNextSongIndex(index, musicFiles.size))

        graduallyIncreaseVolume()
        playFile(musicFiles[index])
    }

    private fun playFile(file: File) {
        if (mediaPlayer != null) {
            stopPlayingSong()
        }
        if (file.extension == "mp3") {
            Log.i("AlarmReceiver", "Preparing to play ${file.name}")
            val path = Uri.fromFile(file)
            val mediaPlayer = MediaPlayer.create(context, path)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.start()
            this.mediaPlayer = mediaPlayer
            Log.i("AlarmReceiver", "Started music")
        }
    }

    fun stopPlayingSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        returnInitialVolume()
    }

    private fun getNextSongIndex(index: Int, maxIndex: Int): Int {
        val newIndex = index + 1
        return if (newIndex >= maxIndex) 0 else newIndex
    }

    private fun getMusicFiles(context: Context): Array<File> =
        context.getExternalFilesDir(musicFilesPath)?.listFiles() ?: emptyArray()

    private fun graduallyIncreaseVolume() {
        val initialVolume = audioManager.getVolume()
        musicProperties.setInitialVolume(initialVolume)

        var volume = 0
        audioManager.setVolume(0)
        val maxVolume = audioManager.getMaxVolume() * cMaxMusicLevel

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                volume += 1
                audioManager.setVolume(volume)
                if (volume < maxVolume) {
                    mainHandler.postDelayed(this, 1000 * cIncreaseVolumeStepSeconds)
                }
            }
        })
    }

    private fun returnInitialVolume() {
        val initialVolume = musicProperties.getInitialVolume().let {
            if (it == -1) audioManager.getVolume() else it
        }
        audioManager.setVolume(initialVolume)
    }

}