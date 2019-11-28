package com.nikstep.alarm

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import com.nikstep.alarm.service.MusicPlayer
import com.nikstep.alarm.service.MusicProperties
import com.nikstep.alarm.service.MyAlarmManager
import com.nikstep.alarm.service.MyAudioManager
import java.lang.String.format
import java.util.Locale

class AlarmActivity : Activity() {

    internal val musicPlayer: MusicPlayer by lazy {
        MusicPlayer(
            applicationContext,
            MyAudioManager(getSystemService(Context.AUDIO_SERVICE) as AudioManager),
            musicProperties
        )
    }
    private val alarmManager: MyAlarmManager by lazy {
        MyAlarmManager(this, getSystemService(Context.ALARM_SERVICE) as AlarmManager, musicPlayer)
    }
    private val musicProperties: MusicProperties by lazy {
        MusicProperties(applicationContext.getSharedPreferences(alarmPropertiesName, 0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (alarmManager.isAlarmActive()) {
            setStatus("Alarm is active")
        } else {
            setStatus("Alarm is not active")
        }
    }

    fun setAlarm(view: View) {
        val hour: Int
        val minute: Int

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }

        alarmManager.setAlarm(hour, minute)
        setStatus("Alarm scheduled at ${format(Locale.ENGLISH, "%02d:%02d", hour, minute)}")
    }

    fun stopAlarm(view: View) {
        musicPlayer.stopPlayingSong()
    }

    fun removeAlarm(view: View) {
        alarmManager.cancelAlarm()
        setStatus("No active alarms")
    }

    private fun setStatus(status: String) {
        findViewById<TextView>(R.id.alarm_status).apply {
            text = status
        }
    }
}
