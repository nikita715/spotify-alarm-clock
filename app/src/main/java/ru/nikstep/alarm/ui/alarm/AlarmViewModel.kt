package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.R
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.log.LogService
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmController: AndroidAlarmController,
    private val playlistDataService: PlaylistDataService,
    private val logService: LogService
) : ViewModel() {

    fun setAlarm(alarmData: AlarmData) {
        alarmController.setAlarm(alarmData)
    }

    fun removeAlarm(alarmId: Long) {
        alarmController.removeAlarm(alarmId)
    }

    fun getPlaylists() = playlistDataService.findAll()

    fun getPlaylist(id: Long) = playlistDataService.findById(id)

    fun getAlarm(alarmId: Long) = alarmController.getAlarm(alarmId)

    fun warnNoPlaylistSelected() = logService.showMessage(R.string.noPlaylistSelected)
}