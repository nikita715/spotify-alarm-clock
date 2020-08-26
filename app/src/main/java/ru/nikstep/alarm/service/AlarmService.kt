package ru.nikstep.alarm.service

import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.model.Alarm

class AlarmService(
    private val alarmDao: AlarmDao
) {

    fun findById(alarmId: Long): Alarm? = alarmDao.findById(alarmId)

    fun findAll(): List<Alarm> = alarmDao.getAll()

    fun save(alarm: Alarm): Alarm {
        val alarmId = alarmDao.insert(alarm)
        return alarmDao.findById(alarmId) ?: throw DataException("Alarm $alarm wasn't saved")
    }

    fun update(alarm: Alarm): Int = alarmDao.update(alarm)

    fun delete(alarmId: Long) = alarmDao.deleteById(alarmId)

    fun updateSettings(alarmData: AlarmData): Alarm {
        alarmDao.updateSettings(
            alarmData.id ?: throw DataException("Alarm id cannot be null"),
            alarmData.hour, alarmData.minute, alarmData.playlist
        )
        return alarmDao.findById(alarmData.id) ?: throw DataException("Alarm ${alarmData.id} not found")
    }

}