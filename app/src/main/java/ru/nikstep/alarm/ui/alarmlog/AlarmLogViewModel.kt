package ru.nikstep.alarm.ui.alarmlog

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.service.data.AlarmLogDataService
import javax.inject.Inject

class AlarmLogViewModel @Inject constructor(
    private val alarmLogDataService: AlarmLogDataService
) : ViewModel() {

    fun getAlarmLogs(): List<AlarmLog> = alarmLogDataService.findAll()

}