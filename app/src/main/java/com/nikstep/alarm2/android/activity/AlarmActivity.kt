package com.nikstep.alarm2.android.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.nikstep.alarm.AlarmReceiver
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.model.Alarm
import com.nikstep.alarm2.service.AlarmService
import com.nikstep.alarm2.service.SongService
import java.time.DayOfWeek
import java.util.Calendar
import java.util.GregorianCalendar

class AlarmActivity : AppCompatActivity() {

    private val alarmManager: AlarmManager by lazy {
        applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val alarmService: AlarmService by lazy {
        Dependencies.get(AlarmService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_creator)
        val songService = Dependencies.get(SongService::class.java)
        val activeSong = songService.findOrCreateActiveSong()
        Log.i("AlarmActivity", activeSong?.title ?: "nothing")
    }

    fun setAlarm(view: View) {
        val hour: Int
        val minute: Int

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }

        alarmService.save(Alarm(-1, hour, minute, DayOfWeek.values().toSet()))

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

        val calendar = GregorianCalendar(year, month, dayOfMonth, hour, minute)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            buildIntent()
        )
    }

    private fun buildIntent(): PendingIntent {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}