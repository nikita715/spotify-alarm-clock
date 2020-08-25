package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmManager
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmManager: AlarmManager
) : ViewModel() {

    fun setAlarm(playlist: String, hour: Int, minute: Int) {
        alarmManager.setAlarm(playlist, hour, minute)
    }

    fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
    }
}