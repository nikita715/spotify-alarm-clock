package com.nikstep.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.VolumeShaper
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.Timer
import java.util.TimerTask

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        context!!

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)

        Log.i("AlarmReceiver", "Received an alarm trigger")
        mediaPlayer?.release()
        mediaPlayer = null

        val musicFiles = context!!.getExternalFilesDir("/Alarm_Music")!!.listFiles()
        val preferences = context.getSharedPreferences("AlarmPref", 0)
        preferences.edit().putInt("InitialVolume", initialVolume)

        var index = preferences.getInt("alarm_song_index", 0)
        index = if (index >= musicFiles.size - 1) 0 else index + 1
        preferences.edit().putInt("alarm_song_index", index).apply()

        val path = Uri.fromFile(musicFiles[index])
        Log.i("AlarmReceiver", "Preparing to play ${musicFiles[index].name}")
        mediaPlayer = MediaPlayer.create(context, path)
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.start()

        Log.i("AlarmReceiver", "Started music")

        var volume = 0
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                volume += 1
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
                if (volume < maxVolume) {
                    mainHandler.postDelayed(this, 5000)
                }
            }
        })
    }
}