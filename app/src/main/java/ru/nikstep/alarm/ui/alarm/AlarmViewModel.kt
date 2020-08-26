package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmData
import ru.nikstep.alarm.service.AlarmManager
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmManager: AlarmManager
) : ViewModel() {

    fun setAlarm(alarmData: AlarmData) {
        alarmManager.setAlarm(alarmData)
    }

    fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
    }

    fun getAlarm(alarmId: Long) = alarmManager.getAlarm(alarmId)
}