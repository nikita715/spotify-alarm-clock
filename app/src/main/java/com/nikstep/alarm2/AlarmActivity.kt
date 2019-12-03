package com.nikstep.alarm2

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.nikstep.alarm.R
import com.nikstep.alarm2.database.SongDatabase

class AlarmActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)
        val songDatabase = SongDatabase(applicationContext)
        val activeSong = songDatabase.findActive()
        Log.i("AlarmActivity", activeSong?.title ?: "nothing")
    }
}