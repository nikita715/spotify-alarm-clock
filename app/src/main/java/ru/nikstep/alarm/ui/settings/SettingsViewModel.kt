package ru.nikstep.alarm.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val alarmController: AlarmController
) : ViewModel() {
    fun deleteAllAlarms() {
        Log.i("SettingsViewModel", "deleteAllAlarms")
    }

    fun updateAlarmReminders() = emitLiveData {

    }

}