package ru.nikstep.alarm.service

import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.model.Alarm

class AlarmService(
    private val alarmDao: AlarmDao
) {

    fun findById(alarmId: Long): Alarm? = alarmDao.findById(alarmId)

    fun findAll(): List<Alarm> = alarmDao.getAll()

    fun save(alarm: Alarm): Long = alarmDao.insert(alarm)

    fun update(alarm: Alarm): Int = alarmDao.update(alarm)

    fun delete(alarmId: Long) = alarmDao.deleteById(alarmId)

    fun deleteAll() = alarmDao.deleteAll()

}