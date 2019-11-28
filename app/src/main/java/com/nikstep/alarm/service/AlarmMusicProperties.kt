package com.nikstep.alarm.service

import android.content.SharedPreferences
import com.nikstep.alarm.pAlarmSongIndex
import com.nikstep.alarm.pInitialVolume

class AlarmMusicProperties(
    private val properties: SharedPreferences
) {
    fun setSongIndex(index: Int) = properties.edit().putInt(pAlarmSongIndex, index).apply()

    fun getSongIndex() = properties.getInt(pAlarmSongIndex, 0)

    fun setInitialVolume(initialVolume: Int) = properties.edit().putInt(pInitialVolume, initialVolume).apply()

    fun getInitialVolume() = properties.getInt(pInitialVolume, -1)
}