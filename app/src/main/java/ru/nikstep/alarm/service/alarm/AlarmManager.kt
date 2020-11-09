package ru.nikstep.alarm.service.alarm

import ru.nikstep.alarm.model.Alarm

/**
 * Wrapper of Android's AlarmManager
 */
interface AlarmManager {
    /**
     * Set an everyday android alarm
     */
    fun enableAlarm(alarm: Alarm)

    /**
     * Remove the everyday android alarm
     */
    fun disableAlarm(alarmId: Long)
    /**
     * Set an everyday android alarm
     */
}