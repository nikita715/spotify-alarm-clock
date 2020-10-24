package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.R
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.log.LogService
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmController: AlarmController,
    private val playlistDataService: PlaylistDataService,
    private val logService: LogService
) : ViewModel() {

    fun setAlarm(alarmData: AlarmData): LiveData<Result<Alarm>> = emitLiveData {
        alarmController.setAlarm(alarmData)
    }

    fun removeAlarm(alarmId: Long) = emitLiveData {
        alarmController.removeAlarm(alarmId)
    }

    fun getPlaylists(): LiveData<Result<List<Playlist>>> = emitLiveData {
        playlistDataService.findAll()
    }

    fun getPlaylist(id: Long): LiveData<Result<Playlist?>> = emitLiveData {
        playlistDataService.findById(id)
    }

    fun getAlarm(alarmId: Long) = emitLiveData {
        alarmController.getAlarm(alarmId)
    }

    fun warnNoPlaylistSelected() = logService.showMessage(R.string.noPlaylistSelected)
}