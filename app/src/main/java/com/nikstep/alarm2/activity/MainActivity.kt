package com.nikstep.alarm2.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.nikstep.alarm.R
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.Dependency
import com.nikstep.alarm2.audio.AlarmMediaPlayer
import com.nikstep.alarm2.database.AlarmDatabase
import com.nikstep.alarm2.database.SongDatabase

class MainActivity : Activity() {

    private val songDatabase by lazy {
        SongDatabase(applicationContext)
    }

    private val alarmDatabase by lazy {
        AlarmDatabase(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        val alarmStatus = alarmDatabase.findById(0)?.run { "$hour:$minute" } ?: "No active alarms"
        findViewById<TextView>(R.id.alarm_status).apply {
            text = alarmStatus
        }
        val activeSong = songDatabase.findActive() ?: songDatabase.findById(0)
        val songStatus = activeSong?.title ?: "No available music"
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
        val alarmMediaPlayer: AlarmMediaPlayer? = Dependencies.get(Dependency.ALARM_MEDIA_PLAYER)
        if (alarmMediaPlayer != null) {
            alarmMediaPlayer.stop()
            Dependencies.remove(Dependency.ALARM_MEDIA_PLAYER)
        }
    }
}