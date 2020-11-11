package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.LoginService
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val alarmController: AlarmController,
    private val loginService: LoginService
) : ViewModel() {

    fun getAlarms() = emitLiveData {
        alarmController.getAllAlarms()
    }

    fun removeAlarm(alarmId: Long) = emitLiveData {
        alarmController.removeAlarm(alarmId)
    }

    fun enableAlarm(alarm: Alarm) = emitLiveData {
        alarmController.enableAlarm(alarm)
    }

    fun disableAlarm(alarmId: Long) = emitLiveData {
        alarmController.disableAlarm(alarmId)
    }

    /**
     * Temporary debug method
     */
    fun play(playlist: String) = alarmController.hackPlay(playlist)

    fun hasAccessToken(): Boolean = loginService.hasAccessToken()

    fun setAccessToken(token: String): Unit = loginService.saveAccessToken(token)
}