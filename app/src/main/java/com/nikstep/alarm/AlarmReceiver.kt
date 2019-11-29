package com.nikstep.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.nikstep.alarm.service.AlarmAudioManager
import com.nikstep.alarm.service.AlarmMusicPlayer
import com.nikstep.alarm.service.AlarmMusicProperties

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val preferences = context.getSharedPreferences(alarmPropertiesName, 0)
            val alarmMusicPlayer = AlarmMusicPlayer(
                context,
                AlarmAudioManager(audioManager),
                AlarmMusicProperties(preferences)
            )
            alarmMusicPlayer.playNextSong()
        }
    }
}