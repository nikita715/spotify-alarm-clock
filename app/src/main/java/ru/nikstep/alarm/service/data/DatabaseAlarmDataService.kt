package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.model.Alarm

class DatabaseAlarmDataService(
    private val alarmDao: AlarmDao
) : AlarmDataService {

    override suspend fun findById(alarmId: Long): Alarm? = alarmDao.findById(alarmId)

    override suspend fun findAll(): List<Alarm> = alarmDao.getAll()

    override suspend fun save(alarm: Alarm): Alarm? =
        if (alarm.id == 0L) create(alarm) else update(alarm)

    override suspend fun delete(alarmId: Long) = alarmDao.deleteById(alarmId)

    private suspend fun create(alarm: Alarm): Alarm? {
        val alarmId = alarmDao.insert(alarm)
        return alarmDao.findById(alarmId)
    }

    private suspend fun update(alarm: Alarm): Alarm? {
        alarmDao.update(alarm)
        return alarmDao.findById(alarm.id)
    }

}