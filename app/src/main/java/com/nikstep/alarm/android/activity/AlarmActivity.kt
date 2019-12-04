package com.nikstep.alarm.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.android.wrapper.AlarmManager
import com.nikstep.alarm.android.wrapper.AlarmMediaPlayer
import com.nikstep.alarm.service.AlarmService

class AlarmActivity : AppCompatActivity() {

    private val alarmManager: AlarmManager by lazy {
        Dependencies.get(AlarmManager::class.java)
    }

    private val alarmService: AlarmService by lazy {
        Dependencies.get(AlarmService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_creator)
    }

    fun setAlarm(view: View) {
        val hour: Int
        val minute: Int

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }
        val daysOfWeek = listOf(
            findViewById<CheckBox>(R.id.day_monday).isChecked,
            findViewById<CheckBox>(R.id.day_tuesday).isChecked,
            findViewById<CheckBox>(R.id.day_wednesday).isChecked,
            findViewById<CheckBox>(R.id.day_thursday).isChecked,
            findViewById<CheckBox>(R.id.day_friday).isChecked,
            findViewById<CheckBox>(R.id.day_saturday).isChecked,
            findViewById<CheckBox>(R.id.day_sunday).isChecked
        ).mapIndexed { i, el -> if (el) i + 1 else -1 }
            .filter { it != -1 }.toSet()

        alarmManager.setAlarm(hour, minute, daysOfWeek)
        goToMainActivity()
    }

    fun removeAlarm(view: View) {
        alarmManager.removeAlarm()
    }

    fun stopAlarm(view: View) {
        Dependencies.getOrNull(AlarmMediaPlayer::class.java)?.stop()
    }

    private fun goToMainActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}