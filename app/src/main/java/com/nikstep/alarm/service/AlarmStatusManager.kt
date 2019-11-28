package com.nikstep.alarm.service

import android.widget.TextView
import java.util.Locale

class AlarmStatusManager(
    private val statusView: TextView,
    private val alarmManager: MyAlarmManager
) {

    fun setAlarmActivityStatus() {
        if (alarmManager.isAlarmActive()) {
            setStatus("Alarm is active")
        } else {
            setStatus("Alarm is not active")
        }
    }

    fun scheduledAt(hour: Int, minute: Int) {
        setStatus("Alarm scheduled at ${java.lang.String.format(Locale.ENGLISH, "%02d:%02d", hour, minute)}")
    }

    fun noActiveAlarms() {
        setStatus("No active alarms")
    }

    private fun setStatus(status: String) {
        statusView.text = status
    }

}