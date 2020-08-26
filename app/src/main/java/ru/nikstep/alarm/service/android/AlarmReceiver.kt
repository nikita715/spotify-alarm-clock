package ru.nikstep.alarm.service.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.service.AlarmController
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onReceive(context: Context?, intent: Intent?) {
        (context?.applicationContext as AlarmApp).androidInjector.inject(this)
        val alarmId = intent?.extras?.getLong("alarmId")
        if (alarmId != null) {
            alarmController.startAlarm(alarmId)
            Log.i("AlarmReceiver", "Alarm $alarmId started")
        } else {
            Log.e("AlarmReceiver", "AlarmId is null")
        }
    }
}