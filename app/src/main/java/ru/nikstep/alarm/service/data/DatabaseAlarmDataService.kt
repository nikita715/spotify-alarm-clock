package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.exception.DataException
import ru.nikstep.alarm.model.Alarm

class DatabaseAlarmDataService(
    private val alarmDao: AlarmDao
) : AlarmDataService {

    override suspend fun findById(alarmId: Long): Alarm? = alarmDao.findById(alarmId)

    override suspend fun findAll(): List<Alarm> = alarmDao.getAll()

    override suspend fun save(alarmData: AlarmData): Alarm =
        if (alarmData.id != null)
            update(alarmData)
        else
            create(Alarm(hour = alarmData.hour, minute = alarmData.minute, playlist = alarmData.playlist))

    override suspend fun delete(alarmId: Long) = alarmDao.deleteById(alarmId)

    override suspend fun create(alarm: Alarm): Alarm {
        val alarmId = alarmDao.insert(alarm)
        return alarmDao.findById(alarmId) ?: throw DataException("Alarm $alarm wasn't saved")
    }

    override suspend fun update(alarm: Alarm): Int = alarmDao.update(alarm)

    private suspend fun update(alarmData: AlarmData): Alarm {
        alarmDao.updateSettings(
            alarmData.id ?: throw DataException("Alarm id cannot be null"),
            alarmData.hour, alarmData.minute, alarmData.playlist
        )
        return alarmDao.findById(alarmData.id) ?: throw DataException("Alarm ${alarmData.id} not found")
    }

}