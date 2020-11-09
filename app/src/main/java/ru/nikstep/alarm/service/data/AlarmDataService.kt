package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.model.Alarm

/**
 * Manages stored alarms
 */
interface AlarmDataService {
    /**
     * Find alarm by id
     */
    suspend fun findById(alarmId: Long): Alarm?

    /**
     * Find all alarms
     */
    suspend fun findAll(): List<Alarm>

    /**
     * Create or update the alarm
     */
    suspend fun save(alarm: Alarm): Alarm?

    /**
     * Delete the alarm
     */
    suspend fun delete(alarmId: Long)

    /**
     * Set [Alarm.nextActive] to false
     */
    suspend fun disableNextAlarm(alarmId: Long)

    /**
     * Set [Alarm.nextActive] to true
     */
    suspend fun enableNextAlarm(alarmId: Long)
}