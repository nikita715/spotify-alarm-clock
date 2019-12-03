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
import com.nikstep.alarm.song.Song
import com.nikstep.alarm.song.currentSong
import java.io.File

class AlarmMusicPlayer(
    private val context: Context,
    private val audioManager: AlarmAudioManager,
    private val alarmMusicProperties: AlarmMusicProperties
) {

    fun playNextSong() {
        Log.i("AlarmReceiver", "Received an alarm trigger")

        val musicFiles = getMusicFiles()

        val index = alarmMusicProperties.getSongIndex()
        alarmMusicProperties.setSongIndex(getNextSongIndex(index, musicFiles.size))

        graduallyIncreaseVolume()
        playFile(musicFiles[index])
    }

    fun getNextSongName(): String? {
        val musicFiles = getMusicFiles()
        val songIndex = alarmMusicProperties.getSongIndex()
        if (musicFiles.isEmpty()) return null
        return (if (musicFiles.size > songIndex) songIndex else 0)
            .let { musicFiles[it].nameWithoutExtension }
    }

    private fun playFile(file: File) {
        if (currentSong != null) {
            stopPlayingSong()
        }
        if (file.extension == "mp3") {
            Log.i("AlarmReceiver", "Preparing to play ${file.name}")
            val path = Uri.fromFile(file)
            val mediaPlayer = MediaPlayer.create(context, path)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.start()
            mediaPlayer.trackInfo
            currentSong = Song(file.nameWithoutExtension, mediaPlayer)
            Log.i("AlarmReceiver", "Started music")
        }
    }

    fun stopPlayingSong() {
        currentSong?.apply {
            stop()
            release()
        }
        currentSong = null
        returnInitialVolume()
    }

    private fun getNextSongIndex(index: Int, maxIndex: Int): Int {
        val newIndex = index + 1
        return if (newIndex >= maxIndex) 0 else newIndex
    }

    fun getMusicFiles(): Array<File> =
        context.getExternalFilesDir(musicFilesPath)?.listFiles() ?: emptyArray()

    private fun graduallyIncreaseVolume() {
        val initialVolume = audioManager.getVolume()
        alarmMusicProperties.setInitialVolume(initialVolume)

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
        val initialVolume = alarmMusicProperties.getInitialVolume().let {
            if (it == -1) audioManager.getVolume() else it
        }
        audioManager.setVolume(initialVolume)
    }

    fun goToNextSong() {
        val songIndex = alarmMusicProperties.getSongIndex()
        alarmMusicProperties.setSongIndex(getNextSongIndex(songIndex, getMusicFiles().size))
    }

}