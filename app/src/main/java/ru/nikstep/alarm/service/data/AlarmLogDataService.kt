package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.model.AlarmLog

/**
 * Manages stored alarm logs
 */
interface AlarmLogDataService {

    /**
     * Find all alarm logs
     */
    suspend fun findAll(): List<AlarmLog>

    /**
     * Create or update the alarm log
     */
    suspend fun save(alarmLog: AlarmLog): Long

    /**
     * Delete all alarm logs
     */
    suspend fun deleteAll()
}