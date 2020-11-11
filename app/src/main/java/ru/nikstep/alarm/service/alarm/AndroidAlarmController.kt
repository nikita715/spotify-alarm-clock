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
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AndroidAlarmController @Inject constructor(
    private val alarmManager: AndroidAlarmManager,
    private val alarmDataService: AlarmDataService,
    private val playlistDataService: PlaylistDataService,
    private val alarmLogDataService: AlarmLogDataService,
    private val spotifyClient: SpotifyClient
) : AlarmController {

    private val isAlarmPlayingNow = AtomicBoolean(false)

    override suspend fun enableAlarm(alarm: Alarm): Alarm? =
        alarmDataService.save(alarm)?.also { savedAlarm ->
            alarmManager.enableAlarm(savedAlarm)
            Log.i("AlarmController", "Saved and enabled $savedAlarm")
        }

    override suspend fun disableAlarm(alarmId: Long): Alarm? {
        enableNextActivationOfAlarm(alarmId)
        alarmManager.disableAlarm(alarmId)
        return alarmDataService.findById(alarmId)
    }

    override suspend fun removeAlarm(alarmId: Long) {
        alarmManager.disableAlarm(alarmId)
        alarmDataService.delete(alarmId)
        Log.i("AlarmController", "Removed alarm by id $alarmId")
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
                    synchronized(isAlarmPlayingNow) {
                        if (it.isPaused.not() && it.playbackSpeed > 0.0f && !isAlarmPlayingNow.get()) {
                            isAlarmPlayingNow.set(true)
                            Log.i("AlarmController", "Alarm is active")
                        } else if (it.isPaused && isAlarmPlayingNow.get()) {
                            alarmManager.stopAlarmService()
                            isAlarmPlayingNow.set(false)
                            Log.i("AlarmController", "Alarm is inactive")
                        }
                    }
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