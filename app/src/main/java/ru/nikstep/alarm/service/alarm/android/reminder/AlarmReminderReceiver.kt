package ru.nikstep.alarm.service.alarm.android.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import javax.inject.Inject

class AlarmReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        startAlarmService(context, intent)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(ALARM_ID_EXTRA, -1L)
        val intentService = Intent(context, AlarmReminderService::class.java).putExtra(ALARM_ID_EXTRA, alarmId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}