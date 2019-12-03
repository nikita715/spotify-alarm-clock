package com.nikstep.alarm.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.nikstep.alarm.song.currentSong

class StopMusicService : IntentService(StopMusicService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == "Close") {
            currentSong?.stop()
            Log.i("StopMusicService", "Stopped music")
        }
    }
}