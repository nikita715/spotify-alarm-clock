package ru.nikstep.alarm.service.alarm

import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm

/**
 * Manages alarm creation, deletion and play
 */
interface AlarmController {
    /**
     * Save the alarm and set android alarm
     */
    fun setAlarm(alarmData: AlarmData)

    /**
     * Remove the alarm and remove android alarm
     */
    fun removeAlarm(alarmId: Long)

    /**
     * Find and start the alarm
     */
    fun startAlarm(alarmId: Long)

    /**
     * Get all stored alarms
     */
    fun getAllAlarms(): List<Alarm>

    /**
     * Get the alarm
     */
    fun getAlarm(alarmId: Long): Alarm?

    /**
     * Temporary debug method
     */
    fun hackPlay(playlist: String)
}