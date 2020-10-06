package ru.nikstep.alarm.ui.main

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.LoginService
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val alarmController: AndroidAlarmController,
    private val loginService: LoginService
) : ViewModel() {

    fun getAlarms() = alarmController.getAllAlarms()

    /**
     * Temporary debug method
     */
    fun play(playlist: String) {
        alarmController.hackPlay(playlist)
    }

    fun removeAlarm(alarmId: Long) = alarmController.removeAlarm(alarmId)

    fun hasAccessToken(): Boolean = loginService.hasAccessToken()

    fun setAccessToken(token: String): Unit = loginService.saveAccessToken(token)

}