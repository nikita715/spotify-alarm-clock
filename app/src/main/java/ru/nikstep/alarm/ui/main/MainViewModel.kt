package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val alarmController: AndroidAlarmController
) : ViewModel() {

    fun getAlarms() = alarmController.getAllAlarms()

    /**
     * Temporary debug method
     */
    fun play(playlist: String) {
        alarmController.hackPlay(playlist)
    }

    fun removeAlarm(alarmId: Long) = alarmController.removeAlarm(alarmId)

}