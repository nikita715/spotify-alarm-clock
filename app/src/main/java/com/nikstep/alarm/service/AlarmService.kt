package com.nikstep.alarm.service

import android.content.Context
import com.nikstep.alarm.database.AlarmDatabase
import com.nikstep.alarm.model.Alarm

class AlarmService(
    context: Context
) {

    private val alarmDatabase = AlarmDatabase(context)

    fun findById(id: Int) = alarmDatabase.findById(id)

    fun save(alarm: Alarm) = alarmDatabase.save(alarm)

    fun delete() = alarmDatabase.delete()

}