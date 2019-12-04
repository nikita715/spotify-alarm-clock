package com.nikstep.alarm.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.android.wrapper.AlarmManager

@RequiresApi(Build.VERSION_CODES.O)
class AlarmReceiver : BroadcastReceiver() {

    private val alarmManager: AlarmManager = Dependencies.get(AlarmManager::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.apply(alarmManager::startAlarm)
    }
}