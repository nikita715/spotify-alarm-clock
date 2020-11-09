package ru.nikstep.alarm.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.R
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.alarm.android.AlarmReceiver
import ru.nikstep.alarm.service.alarm.android.reminder.AlarmReminderReceiver
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.ui.settings.SettingsActivity.Companion.DEFAULT_ALARM_REMINDER_MINUTES
import ru.nikstep.alarm.util.date.buildNextAlarmCalendar
import ru.nikstep.alarm.util.date.buildNextAlarmReminderCalendar
import ru.nikstep.alarm.util.preferences.getAppPreference
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AndroidAlarmManager @Inject constructor(
    private val context: Context
) : ru.nikstep.alarm.service.alarm.AlarmManager {
    private val androidAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun enableAlarm(alarm: Alarm) {
        enableMainAlarm(alarm)
        enableReminderAlarm(alarm)
    }

    private fun enableMainAlarm(alarm: Alarm) = enableEveryDayAlarm(
        alarm.id,
        buildNextAlarmCalendar(alarm.hour, alarm.minute).timeInMillis,
        AlarmReceiver::class.java
    )

    private fun enableReminderAlarm(alarm: Alarm) {
        val minutesBeforeAlarm = context.getAppPreference<Int>(R.string.preference__alarm_reminder_minutes)?.let {
            if (it == -1) DEFAULT_ALARM_REMINDER_MINUTES else it
        } ?: DEFAULT_ALARM_REMINDER_MINUTES
        val reminderCalendar = buildNextAlarmReminderCalendar(
            alarm.hour,
            alarm.minute,
            minutesBeforeAlarm
        )
        enableEveryDayAlarm(
            alarm.id,
            reminderCalendar.timeInMillis,
            AlarmReminderReceiver::class.java
        )
        if (reminderCalendar.before(Calendar.getInstance())) {
            context.sendBroadcast(Intent(context, AlarmReminderReceiver::class.java).putExtra(ALARM_ID_EXTRA, alarm.id))
        }
    }

    private fun enableEveryDayAlarm(id: Long, triggerAtMillis: Long, receiverClass: Class<out BroadcastReceiver>) {
        androidAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            AlarmManager.INTERVAL_DAY,
            buildIntent(id, receiverClass)
        )
        Log.i("AlarmManager", "Alarm scheduled by android at ${Date(triggerAtMillis)}")
    }

    override fun disableAlarm(alarmId: Long) {
        disableMainAlarm(alarmId)
        disableReminderAlarm(alarmId)
    }

    private fun disableMainAlarm(alarmId: Long) =
        disableAlarm(alarmId, AlarmReceiver::class.java)

    private fun disableReminderAlarm(alarmId: Long) =
        disableAlarm(alarmId, AlarmReminderReceiver::class.java)

    private fun disableAlarm(alarmId: Long, receiverClass: Class<out BroadcastReceiver>) {
        androidAlarmManager.cancel(buildIntent(alarmId, receiverClass))
        Log.i("AlarmManager", "Alarm $alarmId removed by android")
    }

    private fun buildIntent(alarmId: Long, receiverClass: Class<out BroadcastReceiver>) = PendingIntent.getBroadcast(
        context, alarmId.toInt(),
        Intent(context, receiverClass).putExtra(ALARM_ID_EXTRA, alarmId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}