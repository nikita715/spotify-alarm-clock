package com.nikstep.alarm.android.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.android.wrapper.AlarmManager

class ActiveAlarmActivity : Activity() {

    private val alarmManager = Dependencies.get(AlarmManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_active)
        findViewById<LinearLayout>(R.id.active_alarm_view).setBackgroundColor(Color.RED)
    }

    override fun onUserInteraction() {
        alarmManager.stopAlarm()
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}