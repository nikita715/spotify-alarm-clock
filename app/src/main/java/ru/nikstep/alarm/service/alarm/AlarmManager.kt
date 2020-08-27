package ru.nikstep.alarm.service.alarm

import ru.nikstep.alarm.model.Alarm

/**
 * Wrapper of Android's AlarmManager
 */
interface AlarmManager {
    /**
     * Set an everyday android alarm
     */
    fun setEveryDayAlarm(alarm: Alarm)

    /**
     * Remove the everyday android alarm
     */
    fun removeAlarm(alarmId: Long)
}