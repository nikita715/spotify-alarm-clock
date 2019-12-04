package com.nikstep.alarm.android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.alarmOneIndex
import com.nikstep.alarm.android.wrapper.AlarmMediaPlayer
import com.nikstep.alarm.service.AlarmService
import com.nikstep.alarm.service.SongService
import java.util.Locale

class MainActivity : Activity() {

    private val songService by lazy {
        Dependencies.get(SongService::class.java)
    }

    private val alarmService by lazy {
        Dependencies.get(AlarmService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmStatus =
            alarmService.findById(alarmOneIndex)?.run {
                String.format(
                    Locale.ENGLISH, "%02d:%02d", hour, minute
                )
            } ?: "No active alarms"
        findViewById<TextView>(R.id.alarm_status).apply {
            text = alarmStatus
        }
        val activeSong = songService.findOrCreateActiveSong()
        val songStatus = activeSong?.run {
            "$singer\n\n$title"
        } ?: "No available music"
        findViewById<TextView>(R.id.song_status).apply {
            text = songStatus
        }
    }

    fun goToAlarms(view: View) {
        startActivity(Intent(this, AlarmActivity::class.java))
    }

    fun goToSongs(view: View) {
        startActivity(Intent(this, SongListActivity::class.java))
    }

    fun stopAlarm(view: View) {
        val alarmMediaPlayer: AlarmMediaPlayer? =
            Dependencies.getOrNull(AlarmMediaPlayer::class.java)
        if (alarmMediaPlayer != null) {
            alarmMediaPlayer.stop()
            Dependencies.remove(AlarmMediaPlayer::class.java)
        }
    }
}