package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmManager
import javax.inject.Inject

class ActivityMainViewModel @Inject constructor(
    private val alarmManager: AlarmManager
) : ViewModel() {

    fun setAlarm(hour: Int, minute: Int) {
        alarmManager.setAlarm(hour, minute)
    }

    fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
    }

}