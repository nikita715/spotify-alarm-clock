package com.nikstep.alarm2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm2.database.AlarmDatabase
import com.nikstep.alarm2.database.SongDatabase
import com.nikstep.alarm2.model.Alarm
import com.nikstep.alarm2.model.Song
import java.io.File

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var alarmDatabase: AlarmDatabase
    private lateinit var songDatabase: SongDatabase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            alarmDatabase = AlarmDatabase(context)
            songDatabase = SongDatabase(context)
            val alarmEntity = getAlarmEntity(intent)
            val resourcesDir = context.getExternalFilesDir(musicFilesPath)
            val activeSong = findActiveSong()
            if (alarmEntity != null && resourcesDir != null && activeSong != null) {
                val songFile = File(resourcesDir.absolutePath + "/" + activeSong.fileName)
                val path = Uri.fromFile(songFile)
                val mediaPlayer = MediaPlayer.create(context, path)
                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer?.start()
                setActiveMediaPlayer(mediaPlayer)
                changeActiveSong(activeSong)
            }
        }
    }

    private fun getAlarmEntity(intent: Intent): Alarm? {
        val alarmIndex = intent.getIntExtra("alarmId", -1)
        if (alarmIndex != -1) {
            return alarmDatabase.findById(alarmIndex)
        }
        return null
    }

    private fun changeActiveSong(activeSong: Song) {
        songDatabase.deactivate(activeSong.id)
        if (songDatabase.exists(activeSong.id + 1)) {
            songDatabase.activate(activeSong.id)
        } else if (songDatabase.exists(0)) {
            songDatabase.activate(0)
        }
    }

    private fun findActiveSong(): Song? {
        val activeSong = songDatabase.findActive()
        return if (activeSong == null) {
            val firstSong = songDatabase.findById(0)
            firstSong
        } else activeSong
    }
}