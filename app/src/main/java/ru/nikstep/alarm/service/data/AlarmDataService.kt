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
    fun findById(alarmId: Long): Alarm?

    /**
     * Find all alarms
     */
    fun findAll(): List<Alarm>

    /**
     * Create or update the alarm
     */
    fun save(alarmData: AlarmData): Alarm

    /**
     * Delete the alarm
     */
    fun delete(alarmId: Long)

    /**
     * Temporary public debug method
     */
    fun create(alarm: Alarm): Alarm

    /**
     * Temporary public debug method
     */
    fun update(alarm: Alarm): Int
}