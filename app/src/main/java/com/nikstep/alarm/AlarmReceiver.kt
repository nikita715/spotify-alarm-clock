package com.nikstep.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.nikstep.alarm.service.AlarmAudioManager
import com.nikstep.alarm.service.AlarmMusicPlayer
import com.nikstep.alarm.service.AlarmMusicProperties
import com.nikstep.alarm.service.StopMusicService

@RequiresApi(Build.VERSION_CODES.O)
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val preferences = context.getSharedPreferences(alarmPropertiesName, 0)
            val alarmMusicPlayer = AlarmMusicPlayer(
                context,
                AlarmAudioManager(audioManager),
                AlarmMusicProperties(preferences)
            )
            alarmMusicPlayer.playNextSong()
            notify(context)
        }
    }

    private fun notify(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "101"

        @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Notification",
            NotificationManager.IMPORTANCE_MAX
        )

        notificationChannel.description = "Game Notifications"
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

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Click to stop music")
            .setContentIntent(action1PendingIntent)
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(1, notificationBuilder.build())
    }
}