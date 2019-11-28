package com.nikstep.alarm

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import com.nikstep.alarm.service.AlarmAudioManager
import com.nikstep.alarm.service.AlarmMusicPlayer
import com.nikstep.alarm.service.AlarmMusicProperties
import com.nikstep.alarm.service.AlarmStatusManager
import com.nikstep.alarm.service.MyAlarmManager

class AlarmActivity : Activity() {
    private val alarmManager: MyAlarmManager by lazy {
        MyAlarmManager(
            this,
            getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmMusicPlayer(
                applicationContext,
                AlarmAudioManager(getSystemService(Context.AUDIO_SERVICE) as AudioManager),
                AlarmMusicProperties(applicationContext.getSharedPreferences(alarmPropertiesName, 0))
            )
        )
    }

    private val alarmStatusManager: AlarmStatusManager by lazy {
        AlarmStatusManager(findViewById(R.id.alarm_status), alarmManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Dependencies.put(alarmManager)
        alarmStatusManager.setAlarmActivityStatus()
    }

    fun setAlarm(view: View) {
        val hour: Int
        val minute: Int

        findViewById<TimePicker>(R.id.time_picker).also { timePicker ->
            hour = timePicker.hour
            minute = timePicker.minute
        }

        val songName = alarmManager.setAlarm(hour, minute)
        alarmStatusManager.nextSongAt(hour, minute, songName)
    }

    fun stopAlarm(view: View) {
        alarmManager.stopPlayingSong()
    }

    fun removeAlarm(view: View) {
        alarmManager.cancelAlarm()
        alarmStatusManager.noActiveAlarms()
    }
}
