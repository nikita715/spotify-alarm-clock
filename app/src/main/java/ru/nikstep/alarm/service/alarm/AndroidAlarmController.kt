package ru.nikstep.alarm.service.alarm

import android.util.Log
import ru.nikstep.alarm.R
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.client.spotify.SpotifyItemType
import ru.nikstep.alarm.client.spotify.SpotifyMusicData
import ru.nikstep.alarm.client.spotify.SpotifyPlayType
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.log.LogService
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.util.date.formatDate
import javax.inject.Inject

class AndroidAlarmController @Inject constructor(
    private val alarmManager: AndroidAlarmManager,
    private val alarmDataService: AlarmDataService,
    private val playlistDataService: PlaylistDataService,
    private val spotifyClient: SpotifyClient,
    private val notificationService: NotificationService,
    private val logService: LogService
) : AlarmController {

    override fun setAlarm(alarmData: AlarmData) {
        val alarm = alarmDataService.save(alarmData)
        alarmManager.setEveryDayAlarm(alarm)
        logService.showMessage(R.string.message_alarm_activated, formatDate(alarm.hour, alarm.minute))
        Log.i("AlarmManager", "Created $alarm")
    }

    override fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
        alarmDataService.delete(alarmId)
        logService.showMessage(R.string.message_alarm_removed)
        Log.i("AlarmManager", "Removed alarm by id $alarmId")
    }

    override fun startAlarm(alarmId: Long) {
        val alarm = alarmDataService.findById(alarmId)?.also { alarm ->
            val playlist = playlistDataService.findById(alarm.playlist)
            if (playlist == null) {
                Log.e("AlarmManager", "Playlist of alarm is null $alarm")
            } else {
                spotifyClient.play(
                    SpotifyMusicData(
                        playlist.externalId,
                        SpotifyItemType.PLAYLIST,
                        SpotifyPlayType.RANDOM
                    ) {
                        val updatedAlarm = alarm.copy(previousTrack = it.track.uri)
                        alarmDataService.update(updatedAlarm)
                        Log.i("AlarmManager", "Saved alarm $updatedAlarm")
                    })
                notificationService.notify("")
            }
        }
        if (alarm == null) {
            Log.e("AlarmManager", "Alarm $alarmId not found")
        }
    }

    override fun stopAlarm() {
        spotifyClient.stop()
    }

    override fun hackPlay(playlist: String) {
        val alarm = alarmDataService.findById(1L)
            ?: Alarm(1, 0, 0, 1, null).also { alarmDataService.create(it) }
        startAlarm(alarm.id)
    }

    override fun getAllAlarms() = alarmDataService.findAll()

    override fun getAlarm(alarmId: Long) = alarmDataService.findById(alarmId)

}