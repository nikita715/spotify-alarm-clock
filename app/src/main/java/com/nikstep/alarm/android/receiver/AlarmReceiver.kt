package com.nikstep.alarm.android.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.alarmOneIndex
import com.nikstep.alarm.android.service.StopMusicService
import com.nikstep.alarm.android.wrapper.AlarmAudioManager
import com.nikstep.alarm.android.wrapper.AlarmMediaPlayer
import com.nikstep.alarm.android.wrapper.AlarmMusicProperties
import com.nikstep.alarm.cIncreaseVolumeStepSeconds
import com.nikstep.alarm.cMaxMusicLevel
import com.nikstep.alarm.instantiateDependencies
import com.nikstep.alarm.model.Alarm
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm.service.AlarmService
import com.nikstep.alarm.service.SongService
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class AlarmReceiver : BroadcastReceiver() {

    private val alarmService: AlarmService = Dependencies.get(AlarmService::class.java)
    private val songService: SongService = Dependencies.get(SongService::class.java)
    private val alarmMusicProperties: AlarmMusicProperties =
        Dependencies.get(AlarmMusicProperties::class.java)
    private val audioManager: AlarmAudioManager = Dependencies.get(AlarmAudioManager::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            instantiateDependencies(context)
            val alarmEntity = getAlarmEntity(intent)
            val resourcesDir = context.getExternalFilesDir(musicFilesPath)
            val activeSong = songService.findOrCreateActiveSong()
            if (alarmEntity != null && resourcesDir != null && activeSong != null) {
                val songFile = File(resourcesDir.absolutePath + "/" + activeSong.fileName)
                val alarmMediaPlayer =
                    AlarmMediaPlayer(context, songFile)
                Dependencies.put(alarmMediaPlayer)
                alarmMediaPlayer.start()
                songService.changeActiveSong(activeSong)
                sendUINotification(context)
            }
        }
    }

    private fun getAlarmEntity(intent: Intent): Alarm? {
        return alarmService.findById(alarmOneIndex)
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

    private fun graduallyIncreaseVolume() {
        val initialVolume = audioManager.getVolume()
        alarmMusicProperties.setInitialVolume(initialVolume)

        var volume = 0
        audioManager.setVolume(0)
        val maxVolume = audioManager.getMaxVolume() * cMaxMusicLevel

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                volume += 1
                audioManager.setVolume(volume)
                if (volume < maxVolume) {
                    mainHandler.postDelayed(this, 1000 * cIncreaseVolumeStepSeconds)
                }
            }
        })
    }

    private fun returnInitialVolume() {
        val initialVolume = alarmMusicProperties.getInitialVolume().let {
            if (it == -1) audioManager.getVolume() else it
        }
        audioManager.setVolume(initialVolume)
    }
}