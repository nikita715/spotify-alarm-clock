package ru.nikstep.alarm.service

import ru.nikstep.alarm.database.AlarmRepository
import ru.nikstep.alarm.model.Alarm

class AlarmService(
    private val alarmDatabase: AlarmRepository
) {

    fun findById(alarmId: Long) = alarmDatabase.findById(alarmId)

    fun findAll() = alarmDatabase.findAll()

    fun save(alarm: Alarm) = alarmDatabase.save(alarm)

    fun delete(alarmId: Long) = alarmDatabase.delete(alarmId)

}