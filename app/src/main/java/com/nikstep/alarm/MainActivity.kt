package com.nikstep.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.GregorianCalendar


class MainActivity : AppCompatActivity() {

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            this, 0, Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    private val alarmManager: AlarmManager by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val preferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences("AlarmPref", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isWorking = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null
        if (isWorking) {
            setStatus("Alarm is active")
        } else {
            setStatus("Alarm is not active")
        }
    }

    fun setAlarm(view: View) {
        val year: Int
        val month: Int
        val dayOfMonth: Int
        val hour: Int
        val minute: Int

        Calendar.getInstance().also { calendar ->
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        }

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }

        val calendar = GregorianCalendar(year, month, dayOfMonth, hour, minute)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            pendingIntent
        )

        setStatus("Alarm scheduled at $hour:${if (minute < 10) "0$minute" else minute}")
    }

    fun stopAlarm(view: View) {
        mediaPlayer?.stop()
        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preferences.getInt("InitialVolume", currentVolume), 0)
    }

    fun removeAlarm(view: View) {
        alarmManager.cancel(pendingIntent)
        setStatus("No active alarms")
    }

    private fun setStatus(status: String) {
        findViewById<TextView>(R.id.alarm_status).apply {
            text = status
        }
    }
}
