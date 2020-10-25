package ru.nikstep.alarm.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.alarm.android.AlarmReceiver
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

class AndroidAlarmManager @Inject constructor(
    private val context: Context
) : ru.nikstep.alarm.service.alarm.AlarmManager {
    private val androidAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setEveryDayAlarm(alarm: Alarm) {
        val calendar = buildCalendar(alarm.hour, alarm.minute)
        androidAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            buildIntent(alarm.id)
        )
        Log.i("AlarmManager", "Alarm scheduled by android at ${alarm.getTimeAsString()}")
    }

    override fun removeAlarm(alarmId: Long) {
        androidAlarmManager.cancel(buildIntent(alarmId))
        Log.i("AlarmManager", "Alarm $alarmId removed by android")
    }

    private fun buildCalendar(hour: Int, minute: Int): GregorianCalendar {
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

        return GregorianCalendar(year, month, dayOfMonth, hour, minute)
    }

    private fun buildIntent(alarmId: Long) = PendingIntent.getBroadcast(
        context, alarmId.toInt(),
        Intent(context, AlarmReceiver::class.java).putExtra(ALARM_ID_EXTRA, alarmId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}