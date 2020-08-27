package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmController: AndroidAlarmController
) : ViewModel() {

    fun setAlarm(alarmData: AlarmData) {
        alarmController.setAlarm(alarmData)
    }

    fun removeAlarm(alarmId: Long) {
        alarmController.removeAlarm(alarmId)
    }

    fun getAlarm(alarmId: Long) = alarmController.getAlarm(alarmId)
}