package com.nikstep.alarm.android.wrapper

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.alarmOneIndex
import com.nikstep.alarm.android.receiver.AlarmReceiver
import com.nikstep.alarm.android.service.StopMusicService
import com.nikstep.alarm.cIncreaseVolumeStepSeconds
import com.nikstep.alarm.cMaxMusicLevel
import com.nikstep.alarm.model.Alarm
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm.service.AlarmService
import com.nikstep.alarm.service.SongService
import java.io.File
import java.util.Calendar
import java.util.GregorianCalendar

class AlarmManager(
    context: Context
) {
    private val androidAlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val alarmMusicProperties by lazy { Dependencies.get(AlarmMusicProperties::class.java) }

    private val audioManager by lazy { Dependencies.get(AlarmAudioManager::class.java) }

    private val songService by lazy { Dependencies.get(SongService::class.java) }

    private val alarmService by lazy { Dependencies.get(AlarmService::class.java) }

    private val intent = Intent(context, AlarmReceiver::class.java)
    private val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    fun setAlarm(hour: Int, minute: Int) {
        removeAlarm()
        alarmService.save(Alarm(1, hour, minute, setOf(1, 2, 3, 4, 5, 6, 7)))

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
            pendingIntent
        )
    }

    fun removeAlarm() {
        androidAlarmManager.cancel(pendingIntent)
        alarmService.delete()
    }

    fun startAlarm(context: Context) {
        val alarmEntity = alarmService.findById(alarmOneIndex)
        val resourcesDir = context.getExternalFilesDir(musicFilesPath)
        val activeSong = songService.findOrCreateActiveSong()
        val currentDayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1)
            .let { if (it < 1) 7 else it }
        if (alarmEntity != null && resourcesDir != null && activeSong != null
            && alarmEntity.daysOfWeek.contains(currentDayOfWeek)
        ) {
            val songFile = File(resourcesDir.absolutePath + "/" + activeSong.fileName)
            val alarmMediaPlayer =
                AlarmMediaPlayer(context, songFile)
            Dependencies.put(alarmMediaPlayer)
            songService.changeActiveSong(activeSong)
            alarmMediaPlayer.start()
            sendUINotification(context)
            graduallyIncreaseVolume()
        }
    }

    private fun graduallyIncreaseVolume() {
        val initialVolume = audioManager.getVolume()
        alarmMusicProperties.setInitialVolume(initialVolume)

        audioManager.setVolume(0)
        val maxVolume = audioManager.getMaxVolume() * cMaxMusicLevel

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                val newVolume = audioManager.getVolume() + 1
                audioManager.setVolume(newVolume)
                if (newVolume < maxVolume) {
                    mainHandler.postDelayed(this, 1000 * cIncreaseVolumeStepSeconds)
                }
            }
        })
    }

    private fun sendUINotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "101"

        val notificationChannel = NotificationChannel(
            notificationChannelId,
            "Notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description = "Alarm Notification"
        notificationChannel.enableLights(true)
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)

        notificationManager.createNotificationChannel(notificationChannel)

        val action1Intent = Intent(context, StopMusicService::class.java)
            .setAction("Close")

        val action1PendingIntent = PendingIntent.getService(
            context, 0,
            action1Intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Click to stop music")
            .setAutoCancel(true)
            .setContentIntent(action1PendingIntent)
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(1, notificationBuilder.build())
    }

    fun stopAlarm() {
        Dependencies.getOrNull(AlarmMediaPlayer::class.java)?.stop()
        returnInitialVolume()
    }

    private fun returnInitialVolume() {
        val initialVolume = alarmMusicProperties.getInitialVolume().let {
            if (it == -1) audioManager.getVolume() else it
        }
        audioManager.setVolume(initialVolume)
    }

}