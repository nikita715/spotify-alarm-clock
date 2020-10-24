package ru.nikstep.alarm.ui.alarmlog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.service.data.AlarmLogDataService
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class AlarmLogViewModel @Inject constructor(
    private val alarmLogDataService: AlarmLogDataService
) : ViewModel() {

    fun getAlarmLogs(): LiveData<Result<List<AlarmLog>>> = emitLiveData {
        alarmLogDataService.findAll()
    }

}