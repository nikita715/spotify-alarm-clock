package com.nikstep.alarm2.service

import android.content.Context
import com.nikstep.alarm2.database.AlarmDatabase
import com.nikstep.alarm2.model.Alarm

class AlarmService(
    context: Context
) {

    private val alarmDatabase = AlarmDatabase(context)

    fun findById(id: Int) = alarmDatabase.findById(id)

    fun save(alarm: Alarm) = alarmDatabase.save(alarm)

}