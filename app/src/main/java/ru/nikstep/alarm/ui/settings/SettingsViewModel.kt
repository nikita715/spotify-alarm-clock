package ru.nikstep.alarm.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.service.alarm.AlarmController
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    alarmController: AlarmController
) : ViewModel() {
    fun deleteAllAlarms() {
        Log.i("SettingsViewModel", "deleteAllAlarms")
    }

}