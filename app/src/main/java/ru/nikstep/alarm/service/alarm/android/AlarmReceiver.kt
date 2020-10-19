package ru.nikstep.alarm.service.alarm.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        } else {
            startAlarmService(context, intent)
        }
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(ALARM_ID_EXTRA, -1L)
        val intentService = Intent(context, AlarmService::class.java).putExtra(ALARM_ID_EXTRA, alarmId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}