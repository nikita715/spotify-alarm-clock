package ru.nikstep.alarm.ui.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val alarmController: AlarmController,
    private val playlistDataService: PlaylistDataService
) : ViewModel() {

    fun setAlarm(alarm: Alarm): LiveData<Result<Alarm?>> = emitLiveData {
        alarmController.enableAlarm(alarm)
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
}