package com.nikstep.alarm2.android.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.audio.AlarmMediaPlayer

class StopMusicService : IntentService(StopMusicService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == "Close") {
            Dependencies.getOrNull(AlarmMediaPlayer::class.java)?.stop()
            Log.i("StopMusicService", "Stopped music")
        }
    }
}