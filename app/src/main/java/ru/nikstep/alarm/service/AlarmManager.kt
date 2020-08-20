package ru.nikstep.alarm.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import ru.nikstep.alarm.BuildConfig
import ru.nikstep.alarm.model.Alarm
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

class AlarmManager @Inject constructor(
    private val context: Context,
    private val alarmService: AlarmService
) {
    private val androidAlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(
        hour: Int,
        minute: Int
    ) {
        val alarm = alarmService.save(Alarm(id = 1L, hour = hour, minute = minute))

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

        val calendar = GregorianCalendar(year, month, dayOfMonth, hour, minute)
        androidAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            buildIntent(alarm.id)
        )
    }

    fun removeAlarm(alarmId: Long) {
        androidAlarmManager.cancel(buildIntent(alarmId))
        alarmService.delete(alarmId)
    }

    fun startAlarm(alarmId: Long) {
        val playlist =
//            alarmService.findById(alarmId)?.playlist ?:
            BuildConfig.DEFAULT_PLAYLIST
        val connectionParams = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
            .setRedirectUri(BuildConfig.SPOTIFY_REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                appRemote.playerApi.play("spotify:playlist:$playlist", PlayerApi.StreamType.ALARM)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
    }

    private fun buildIntent(alarmId: Long) = PendingIntent.getBroadcast(
        context, 0,
        Intent(context, AlarmReceiver::class.java).putExtra("alarmId", alarmId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}