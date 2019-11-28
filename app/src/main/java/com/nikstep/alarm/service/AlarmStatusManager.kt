package com.nikstep.alarm.service

import android.widget.TextView
import java.util.Locale

class AlarmStatusManager(
    private val statusView: TextView
) {

    fun setAlarmActivityStatus(active: Boolean) {
        if (active) {
            setStatus("Alarm is active")
        } else {
            setStatus("Alarm is not active")
        }
    }

    fun nextSongAt(hour: Int, minute: Int, songName: String?) {
        setStatus(
            "Alarm scheduled at ${java.lang.String.format(Locale.ENGLISH, "%02d:%02d", hour, minute)}." +
                    " Song: $songName"
        )
    }

    fun noActiveAlarms() {
        setStatus("No active alarms")
    }

    private fun setStatus(status: String) {
        statusView.text = status
    }

}