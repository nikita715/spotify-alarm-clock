package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val alarmManager: AlarmManager
) : ViewModel() {

    fun getAlarms() = alarmManager.getAllAlarms()

    fun play(playlist: String) {
        alarmManager.hackPlay(playlist)
    }

    fun removeAlarm(alarmId: Long) = alarmManager.removeAlarm(alarmId)

}