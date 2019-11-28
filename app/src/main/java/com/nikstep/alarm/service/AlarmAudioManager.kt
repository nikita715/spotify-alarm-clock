package com.nikstep.alarm.service

import android.media.AudioManager
import com.nikstep.alarm.alarmStreamType

class AlarmAudioManager(
    private val audioManager: AudioManager
) {

    fun getVolume() = audioManager.getStreamVolume(alarmStreamType)

    fun setVolume(volume: Int) = audioManager.setStreamVolume(alarmStreamType, volume, 0)

    fun getMaxVolume() = audioManager.getStreamMaxVolume(alarmStreamType)

}