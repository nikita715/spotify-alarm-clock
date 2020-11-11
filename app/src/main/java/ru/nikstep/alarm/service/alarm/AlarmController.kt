package ru.nikstep.alarm.service.alarm

import ru.nikstep.alarm.client.spotify.SpotifyMusicData
import ru.nikstep.alarm.model.Alarm

/**
 * Manages alarm creation, deletion and play
 */
interface AlarmController {

    /**
     * Save the alarm, set the android alarm and set the android reminder alarm if necessary
     */
    suspend fun enableAlarm(alarm: Alarm): Alarm?

    /**
     * Disable the android alarm and disable the android reminder alarm
     */
    suspend fun disableAlarm(alarmId: Long): Alarm?

    /**
     * Disable the android alarm, disable the android reminder alarm and remove the alarm
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
     * Disables next activation of alarm
     */
    suspend fun disableNextActivationOfAlarm(alarmId: Long)

    /**
     * Disables next activation of alarm
     */
    suspend fun enableNextActivationOfAlarm(alarmId: Long)

    /**
     * Temporary debug method
     */
    fun hackPlay(playlist: String)
}