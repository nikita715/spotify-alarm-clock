package com.nikstep.alarm.android.wrapper

import android.content.Context
import android.content.SharedPreferences
import com.nikstep.alarm.alarmPropertiesName
import com.nikstep.alarm.pAlarmSongIndex
import com.nikstep.alarm.pInitialVolume

class AlarmMusicProperties(context: Context) {
    private val properties: SharedPreferences = context.getSharedPreferences(alarmPropertiesName, 0)

    fun setSongIndex(index: Int) = properties.edit().putInt(pAlarmSongIndex, index).apply()

    fun getSongIndex() = properties.getInt(pAlarmSongIndex, 0)

    fun setInitialVolume(initialVolume: Int) =
        properties.edit().putInt(pInitialVolume, initialVolume).apply()

    fun getInitialVolume() = properties.getInt(pInitialVolume, -1)
}