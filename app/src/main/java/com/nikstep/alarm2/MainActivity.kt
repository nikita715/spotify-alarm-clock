package com.nikstep.alarm2

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.nikstep.alarm.R
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
}