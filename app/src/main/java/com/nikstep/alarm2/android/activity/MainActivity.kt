package com.nikstep.alarm2.android.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.audio.AlarmMediaPlayer
import com.nikstep.alarm2.service.AlarmService
import com.nikstep.alarm2.service.SongService

class MainActivity : Activity() {

    private val songService by lazy {
        Dependencies.get(SongService::class.java)
    }

    private val alarmService by lazy {
        Dependencies.get(AlarmService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        val alarmStatus = alarmService.findById(0)?.run { "$hour:$minute" } ?: "No active alarms"
        findViewById<TextView>(R.id.alarm_status).apply {
            text = alarmStatus
        }
        val activeSong = songService.findOrCreateActiveSong()
        val songStatus = activeSong?.run {
            "$singer - $title"
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

    private fun findOrUpdateActiveSong() {

    }
}