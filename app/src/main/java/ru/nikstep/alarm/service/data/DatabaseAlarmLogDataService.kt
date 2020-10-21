package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.database.AlarmLogDao
import ru.nikstep.alarm.model.AlarmLog

class DatabaseAlarmLogDataService(
    private val alarmLogDao: AlarmLogDao
) : AlarmLogDataService {
    override fun findAll(): List<AlarmLog> = alarmLogDao.getAll()

    override fun save(alarmLog: AlarmLog): Long = alarmLogDao.insert(alarmLog)

    override fun deleteAll(): Unit = alarmLogDao.deleteAll()
}