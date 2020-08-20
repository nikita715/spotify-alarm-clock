package ru.nikstep.alarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.AlarmApp
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.applicationContext as AlarmApp).androidInjector.inject(this)
        val alarmId = intent?.extras?.getLong("alarmId")
        if (alarmId != null) {
            alarmManager.startAlarm(alarmId)
            Log.i("AlarmReceiver", "Alarm $alarmId started")
        } else {
            Log.e("AlarmReceiver", "AlarmId is null")
        }
    }
}