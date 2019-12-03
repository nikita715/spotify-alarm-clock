package com.nikstep.alarm2.android.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm.service.StopMusicService
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.audio.AlarmMediaPlayer
import com.nikstep.alarm2.instantiateDependencies
import com.nikstep.alarm2.model.Alarm
import com.nikstep.alarm2.service.AlarmService
import com.nikstep.alarm2.service.SongService
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class AlarmReceiver : BroadcastReceiver() {

    private lateinit var alarmService: AlarmService
    private lateinit var songService: SongService

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            instantiateDependencies(context)
            alarmService = Dependencies.get(AlarmService::class.java)
            songService = Dependencies.get(SongService::class.java)
            val alarmEntity = getAlarmEntity(intent)
            val resourcesDir = context.getExternalFilesDir(musicFilesPath)
            val activeSong = songService.findOrCreateActiveSong()
            if (alarmEntity != null && resourcesDir != null && activeSong != null) {
                val songFile = File(resourcesDir.absolutePath + "/" + activeSong.fileName)
                val alarmMediaPlayer = AlarmMediaPlayer(context, songFile)
                Dependencies.put(alarmMediaPlayer)
                alarmMediaPlayer.start()
                songService.changeActiveSong(activeSong)
                sendUINotification(context)
            }
        }
    }

    private fun getAlarmEntity(intent: Intent): Alarm? {
        val alarmIndex = intent.getIntExtra("alarmId", -1)
        if (alarmIndex != -1) {
            return alarmService.findById(alarmIndex)
        }
        return null
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
}