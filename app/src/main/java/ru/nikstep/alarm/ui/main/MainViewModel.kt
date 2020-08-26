package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.AlarmController
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val alarmController: AlarmController
) : ViewModel() {

    fun getAlarms() = alarmController.getAllAlarms()

    fun play(playlist: String) {
        alarmController.hackPlay(playlist)
    }

    fun removeAlarm(alarmId: Long) = alarmController.removeAlarm(alarmId)

}