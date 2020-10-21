package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.model.AlarmLog

/**
 * Manages stored alarm logs
 */
interface AlarmLogDataService {

    /**
     * Find all alarm logs
     */
    fun findAll(): List<AlarmLog>

    /**
     * Create or update the alarm log
     */
    fun save(alarmLog: AlarmLog): Long

    /**
     * Delete all alarm logs
     */
    fun deleteAll()
}