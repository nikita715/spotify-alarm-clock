package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.data.AlarmData
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
    suspend fun save(alarmData: AlarmData): Alarm

    /**
     * Delete the alarm
     */
    suspend fun delete(alarmId: Long)

    /**
     * Temporary public debug method
     */
    suspend fun create(alarm: Alarm): Alarm

    /**
     * Temporary public debug method
     */
    suspend fun update(alarm: Alarm): Int
}