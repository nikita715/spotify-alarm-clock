package com.nikstep.alarm.android.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.android.wrapper.AlarmManager

class StopMusicService : IntentService(StopMusicService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == "Close") {
            Dependencies.get(AlarmManager::class.java).stopAlarm()
            Log.i("StopMusicService", "Stopped music")
        }
    }
}