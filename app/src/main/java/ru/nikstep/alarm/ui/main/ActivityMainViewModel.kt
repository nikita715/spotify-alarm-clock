package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmManager
import javax.inject.Inject

class ActivityMainViewModel @Inject constructor(
    private val alarmManager: AlarmManager
) : ViewModel() {

    fun setAlarm(playlist: String, hour: Int, minute: Int) {
        alarmManager.setAlarm(playlist, hour, minute)
    }

    fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
    }

}