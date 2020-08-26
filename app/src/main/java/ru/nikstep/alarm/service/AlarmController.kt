package ru.nikstep.alarm.service

import android.util.Log
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.client.spotify.SpotifyData
import ru.nikstep.alarm.client.spotify.SpotifyItemType
import ru.nikstep.alarm.client.spotify.SpotifyPlayType
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.android.AlarmManager
import javax.inject.Inject

class AlarmController @Inject constructor(
    private val alarmManager: AlarmManager,
    private val alarmService: AlarmService,
    private val spotifyClient: SpotifyClient
) {

    fun setAlarm(alarmData: AlarmData) {
        val alarm = if (alarmData.id != null)
            alarmService.updateSettings(alarmData)
        else
            alarmService.save(Alarm(hour = alarmData.hour, minute = alarmData.minute, playlist = alarmData.playlist))
        alarmManager.setEveryDayAlarm(alarm)
        Log.i("AlarmManager", "Created $alarm")
    }

    fun removeAlarm(alarmId: Long) {
        alarmManager.removeAlarm(alarmId)
        alarmService.delete(alarmId)
        Log.i("AlarmManager", "Removed  alarm by id $alarmId")
    }

    fun startAlarm(alarmId: Long) {
        val alarm = alarmService.findById(alarmId)?.also { alarm ->
            val playlist = alarm.playlist
            if (playlist == null) {
                Log.e("AlarmManager", "Playlist of alarm is null $alarm")
            } else {
                spotifyClient.play(SpotifyData(playlist, SpotifyItemType.PLAYLIST, SpotifyPlayType.RANDOM))
            }
        }
        if (alarm == null) {
            Log.e("AlarmManager", "Alarm $alarmId not found")
        }
    }

    fun hackPlay(playlist: String) {
        val alarm = alarmService.findById(1L)
            ?: Alarm(1, 0, 0, playlist, null).also { alarmService.save(it) }
        spotifyClient.play(
            SpotifyData(playlist, SpotifyItemType.PLAYLIST, SpotifyPlayType.RANDOM, alarm.previousTrack) {
                val updatedAlarm = alarm.copy(previousTrack = it.track.uri)
                alarmService.update(updatedAlarm)
                Log.i("AlarmManager", "Saved alarm $updatedAlarm")
            })
    }

    fun getAllAlarms() = alarmService.findAll()

    fun getAlarm(alarmId: Long) = alarmService.findById(alarmId)

}