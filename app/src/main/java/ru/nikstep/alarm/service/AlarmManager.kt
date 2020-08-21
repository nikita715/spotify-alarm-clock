package ru.nikstep.alarm.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.client.spotify.SpotifyItemType
import ru.nikstep.alarm.model.Alarm
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

class AlarmManager @Inject constructor(
    private val context: Context,
    private val alarmService: AlarmService,
    private val spotifyClient: SpotifyClient
) {
    private val androidAlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(playlist: String, hour: Int, minute: Int) {
        val alarmId = alarmService.save(Alarm(hour = hour, minute = minute, playlist = playlist))
        val alarm = alarmService.findById(alarmId)!!

        val calendar = buildAlarmCalendar(hour, minute)

        androidAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            buildIntent(alarm.id)
        )
        Log.i("AlarmManager", "Created $alarm")
    }

    private fun buildAlarmCalendar(hour: Int, minute: Int): GregorianCalendar {
        val year: Int
        val month: Int
        val dayOfMonth: Int

        Calendar.getInstance().also { calendar ->
            if (calendar.get(Calendar.HOUR_OF_DAY) >= hour && calendar.get(Calendar.MINUTE) >= minute) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        }

        return GregorianCalendar(year, month, dayOfMonth, hour, minute)
    }

    fun removeAlarm(alarmId: Long) {
        alarmService.findAll().forEach {
            androidAlarmManager.cancel(buildIntent(it.id))
        }
        alarmService.deleteAll()
        Log.i("AlarmManager", "Removed all alarms")
    }

    fun startAlarm(alarmId: Long) {
        val alarm = alarmService.findById(alarmId)
        if (alarm == null) {
            Log.e("AlarmManager", "Alarm $alarmId not found")
        } else {
            val playlist = alarm.playlist
            if (playlist == null) {
                Log.e("AlarmManager", "Playlist of alarm is null $alarm")
            } else {
                spotifyClient.play(playlist, SpotifyItemType.PLAYLIST)
            }
        }
    }

    private fun buildIntent(alarmId: Long) = PendingIntent.getBroadcast(
        context, 0,
        Intent(context, AlarmReceiver::class.java).putExtra("alarmId", alarmId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    fun play(playlist: String) {
        val alarm = alarmService.findById(1L)
        if (alarm == null) {
            Log.e("AlarmManager", "Alarm 1 is null")
            return
        }
        spotifyClient.play(playlist, SpotifyItemType.PLAYLIST, alarm.previousTrack) {
            val updatedAlarm = alarm.copy(previousTrack = it.track.uri)
            alarmService.update(updatedAlarm)
            Log.i("AlarmManager", "Saved alarm $updatedAlarm")
        }
    }

}