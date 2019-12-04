package com.nikstep.alarm.android.wrapper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nikstep.alarm.android.receiver.AlarmReceiver
import java.util.GregorianCalendar

class AlarmManager(
    context: Context
) {
    private val androidAlarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val intent = Intent(context, AlarmReceiver::class.java)
    private val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    fun setAlarm(calendar: GregorianCalendar) {
        removeAlarm()
        androidAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            pendingIntent
        )
    }

    fun removeAlarm() {
        androidAlarmManager.cancel(pendingIntent)
    }

}