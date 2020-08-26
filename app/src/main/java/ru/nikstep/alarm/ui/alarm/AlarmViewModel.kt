package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmController
import ru.nikstep.alarm.service.AlarmData
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmController: AlarmController
) : ViewModel() {

    fun setAlarm(alarmData: AlarmData) {
        alarmController.setAlarm(alarmData)
    }

    fun removeAlarm(alarmId: Long) {
        alarmController.removeAlarm(alarmId)
    }

    fun getAlarm(alarmId: Long) = alarmController.getAlarm(alarmId)
}