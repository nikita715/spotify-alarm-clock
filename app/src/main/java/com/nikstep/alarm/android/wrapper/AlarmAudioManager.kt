package com.nikstep.alarm.android.wrapper

import android.content.Context
import android.media.AudioManager
import com.nikstep.alarm.alarmStreamType

class AlarmAudioManager(context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun getVolume() = audioManager.getStreamVolume(alarmStreamType)

    fun setVolume(volume: Int) = audioManager.setStreamVolume(alarmStreamType, volume, 0)

    fun getMaxVolume() = audioManager.getStreamMaxVolume(alarmStreamType)

}