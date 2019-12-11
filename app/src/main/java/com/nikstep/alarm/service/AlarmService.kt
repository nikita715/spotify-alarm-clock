package com.nikstep.alarm.service

import com.nikstep.alarm.database.AlarmDatabase
import com.nikstep.alarm.model.Alarm

class AlarmService(
    private val alarmDatabase: AlarmDatabase
) {

    fun findById(id: Int) = alarmDatabase.findById(id)

    fun save(alarm: Alarm) = alarmDatabase.save(alarm)

    fun delete() = alarmDatabase.delete()

}