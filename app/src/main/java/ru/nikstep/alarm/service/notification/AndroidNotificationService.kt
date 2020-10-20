package ru.nikstep.alarm.service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ru.nikstep.alarm.R
import ru.nikstep.alarm.service.alarm.android.StopAlarmService
import ru.nikstep.alarm.service.alarm.android.StopAlarmService.Companion.CLOSE_ALARM_ACTION
import ru.nikstep.alarm.ui.main.MainActivity.Companion.CHANNEL_ID

class AndroidNotificationService(private val context: Context) : NotificationService {
    override fun buildAlarmNotification(): Notification =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Click to stop the music")
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getService(
                    context, 0,
                    Intent(context, StopAlarmService::class.java)
                        .setAction(CLOSE_ALARM_ACTION), PendingIntent.FLAG_ONE_SHOT
                )
            )
            .setWhen(System.currentTimeMillis())
            .build()
}