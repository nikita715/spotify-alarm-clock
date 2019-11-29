package com.nikstep.alarm

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import com.nikstep.alarm.service.AlarmAudioManager
import com.nikstep.alarm.service.AlarmMusicPlayer
import com.nikstep.alarm.service.AlarmMusicProperties
import com.nikstep.alarm.service.AlarmStatusManager
import com.nikstep.alarm.service.MyAlarmManager

class AlarmActivity : Activity() {
    private val alarmManager: MyAlarmManager by lazy {
        val androidAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val androidAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val statusView = findViewById<TextView>(R.id.alarm_status)
        val preferences = applicationContext.getSharedPreferences(alarmPropertiesName, 0)
        MyAlarmManager(
            this,
            androidAlarmManager,
            AlarmStatusManager(statusView),
            AlarmMusicPlayer(
                applicationContext,
                AlarmAudioManager(androidAudioManager),
                AlarmMusicProperties(preferences)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Dependencies.put(alarmManager)
        alarmManager.onCreateAlarmActivity()
    }

    fun setAlarm(view: View) {
        val hour: Int
        val minute: Int

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }

        alarmManager.setAlarm(hour, minute)
    }

    fun stopAlarm(view: View) {
        alarmManager.stopPlayingSong()
    }

    fun goToNextSong(view: View) {
        alarmManager.goToNextSong()
    }

    fun removeAlarm(view: View) {
        alarmManager.cancelAlarm()
    }
}
