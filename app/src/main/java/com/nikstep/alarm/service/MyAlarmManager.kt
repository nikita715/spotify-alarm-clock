package com.nikstep.alarm.service

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import com.nikstep.alarm.AlarmReceiver
import java.util.Calendar
import java.util.GregorianCalendar

class MyAlarmManager(
    private val activity: Activity,
    private val alarmManager: AlarmManager,
    private val alarmMusicPlayer: AlarmMusicPlayer
) {
    private val pendingIntent: PendingIntent by lazy {
        val intent = Intent(activity, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(
            activity, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun setAlarm(hour: Int, minute: Int) {
        val year: Int
        val month: Int
        val dayOfMonth: Int

        Calendar.getInstance().also { calendar ->
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val calendar = GregorianCalendar(year, month, dayOfMonth, hour, minute)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            pendingIntent
        )
    }

    fun playNextSong() {
        alarmMusicPlayer.playNextSong()
    }

    fun stopPlayingSong() {
        alarmMusicPlayer.stopPlayingSong()
    }

    fun cancelAlarm() {
        alarmManager.cancel(pendingIntent)
    }

    fun isAlarmActive() = PendingIntent.getBroadcast(
        activity, 0, Intent(activity, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE
    ) != null
}