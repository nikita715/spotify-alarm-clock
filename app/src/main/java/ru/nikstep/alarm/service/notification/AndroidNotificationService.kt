package ru.nikstep.alarm.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.nikstep.alarm.R
import ru.nikstep.alarm.service.alarm.StopAlarmService
import ru.nikstep.alarm.ui.main.MainActivity.Companion.CHANNEL_ID

class AndroidNotificationService(private val context: Context) : NotificationService {
    override fun notify(text: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "101"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = "Android Alarm App"
            notificationChannel.enableLights(true)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(context, StopAlarmService::class.java)
            .setAction("Close")

        val action1PendingIntent = PendingIntent.getService(
            context, 0,
            intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Click to stop music")
            .setAutoCancel(true)
            .setContentIntent(action1PendingIntent)
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(1, notificationBuilder.build())
    }
}