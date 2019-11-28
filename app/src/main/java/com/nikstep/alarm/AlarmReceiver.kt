package com.nikstep.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nikstep.alarm.service.MyAlarmManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val musicPlayer = Dependencies.get(MyAlarmManager::class.java)
        musicPlayer?.playNextSong()
    }
}