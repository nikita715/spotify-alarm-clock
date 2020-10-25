package ru.nikstep.alarm.service.alarm

import ru.nikstep.alarm.client.spotify.SpotifyMusicData
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm

/**
 * Manages alarm creation, deletion and play
 */
interface AlarmController {
    /**
     * Save the alarm and set android alarm
     */
    suspend fun setAlarm(alarm: Alarm): Alarm?

    /**
     * Remove the alarm and remove android alarm
     */
    suspend fun removeAlarm(alarmId: Long)

    /**
     * Get all stored alarms
     */
    suspend fun getAllAlarms(): List<Alarm>

    /**
     * Get the alarm
     */
    suspend fun getAlarm(alarmId: Long): Alarm?

    /**
     * Create [SpotifyMusicData] for spotify
     */
    suspend fun buildSpotifyMusicData(alarmId: Long): SpotifyMusicData?

    /**
     * Start the alarm
     */
    fun startAlarm(spotifyMusicData: SpotifyMusicData)

    /**
     * Find and start the alarm
     */
    fun stopAlarm()

    /**
     * Temporary debug method
     */
    fun hackPlay(playlist: String)
}