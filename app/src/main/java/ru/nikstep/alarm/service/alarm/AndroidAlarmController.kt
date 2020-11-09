package ru.nikstep.alarm.service.alarm

import android.util.Log
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.client.spotify.SpotifyItemType
import ru.nikstep.alarm.client.spotify.SpotifyMusicData
import ru.nikstep.alarm.client.spotify.SpotifyPlayType
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.service.data.AlarmLogDataService
import ru.nikstep.alarm.service.data.PlaylistDataService
import javax.inject.Inject

class AndroidAlarmController @Inject constructor(
    private val alarmManager: AndroidAlarmManager,
    private val alarmDataService: AlarmDataService,
    private val playlistDataService: PlaylistDataService,
    private val alarmLogDataService: AlarmLogDataService,
    private val spotifyClient: SpotifyClient
) : AlarmController {

    override suspend fun enableAlarm(alarm: Alarm): Alarm? =
        alarmDataService.save(alarm)?.also { savedAlarm ->
            alarmManager.enableAlarm(savedAlarm)
            Log.i("AlarmManager", "Saved and enabled $savedAlarm")
        }

    override suspend fun disableAlarm(alarm: Alarm): Alarm? =
        alarmDataService.save(alarm)?.also { savedAlarm ->
            alarmManager.disableAlarm(savedAlarm.id)
            Log.i("AlarmManager", "Saved and disabled $savedAlarm")
        }

    override suspend fun removeAlarm(alarmId: Long) {
        alarmManager.disableAlarm(alarmId)
        alarmDataService.delete(alarmId)
        Log.i("AlarmManager", "Removed alarm by id $alarmId")
    }

    override suspend fun buildSpotifyMusicData(alarmId: Long): SpotifyMusicData? =
        alarmDataService.findById(alarmId)?.let { alarm ->
            playlistDataService.findById(alarm.playlist)?.let { playlist ->
                alarmLogDataService.save(AlarmLog(alarmId = alarmId, playlist = playlist.name))
                SpotifyMusicData(
                    id = playlist.externalId,
                    type = SpotifyItemType.PLAYLIST,
                    playType = SpotifyPlayType.RANDOM,
                    enabled = alarm.nextActive
                ) {
                    val updatedAlarm = alarm.copy(previousTrack = it.track.uri)
                    alarmDataService.save(updatedAlarm)
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

    override suspend fun disableNextActivationOfAlarm(alarmId: Long) = alarmDataService.disableNextAlarm(alarmId)

    override suspend fun enableNextActivationOfAlarm(alarmId: Long) = alarmDataService.enableNextAlarm(alarmId)

    override fun hackPlay(playlist: String) {
        startAlarm(SpotifyMusicData(playlist, SpotifyItemType.PLAYLIST, SpotifyPlayType.RANDOM))
    }

    override suspend fun getAllAlarms() = alarmDataService.findAll()

    override suspend fun getAlarm(alarmId: Long) = alarmDataService.findById(alarmId)

}