package com.nikstep.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context is AlarmActivity) {
            val musicPlayer = context.musicPlayer
            musicPlayer.playNextSong()
        }
    }
}