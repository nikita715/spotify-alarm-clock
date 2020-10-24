package ru.nikstep.alarm.service.alarm

import android.util.Log
import ru.nikstep.alarm.R
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.client.spotify.SpotifyItemType
import ru.nikstep.alarm.client.spotify.SpotifyMusicData
import ru.nikstep.alarm.client.spotify.SpotifyPlayType
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.service.data.AlarmLogDataService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.log.LogService
import ru.nikstep.alarm.util.date.formatDate
import javax.inject.Inject

class AndroidAlarmController @Inject constructor(
    private val alarmManager: AndroidAlarmManager,
    private val alarmDataService: AlarmDataService,
    private val playlistDataService: PlaylistDataService,
    private val alarmLogDataService: AlarmLogDataService,
    private val spotifyClient: SpotifyClient,
    private val logService: LogService
) : AlarmController {

    override suspend fun setAlarm(alarmData: AlarmData): Alarm {
        val alarm = alarmDataService.save(alarmData)
        alarmManager.setEveryDayAlarm(alarm)
        logService.showMessage(R.string.message_alarm_activated, formatDate(alarm.hour, alarm.minute))
        Log.i("AlarmManager", "Created $alarm")
        return alarm
    }

    override suspend fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
        alarmDataService.delete(alarmId)
        logService.showMessage(R.string.message_alarm_removed)
        Log.i("AlarmManager", "Removed alarm by id $alarmId")
    }

    override suspend fun buildSpotifyMusicData(alarmId: Long): SpotifyMusicData? =
        alarmDataService.findById(alarmId)?.let { alarm ->
            playlistDataService.findById(alarm.playlist)?.let { playlist ->
                alarmLogDataService.save(AlarmLog(alarmId = alarmId, playlist = playlist.name))
                SpotifyMusicData(
                    playlist.externalId,
                    SpotifyItemType.PLAYLIST,
                    SpotifyPlayType.RANDOM
                ) {
                    val updatedAlarm = alarm.copy(previousTrack = it.track.uri)
                    alarmDataService.update(updatedAlarm)
                    Log.i("AlarmManager", "Saved alarm $updatedAlarm")
                }
            }
        }

    override fun startAlarm(spotifyMusicData: SpotifyMusicData) {
        spotifyClient.play(spotifyMusicData)
    }

    override fun stopAlarm() {
        spotifyClient.stop()
    }

    override fun hackPlay(playlist: String) {
        startAlarm(SpotifyMusicData(playlist, SpotifyItemType.PLAYLIST, SpotifyPlayType.RANDOM))
    }

    override suspend fun getAllAlarms() = alarmDataService.findAll()

    override suspend fun getAlarm(alarmId: Long) = alarmDataService.findById(alarmId)

}