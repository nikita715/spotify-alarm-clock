package ru.nikstep.alarm.service.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ru.nikstep.alarm.AlarmApp
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmController: AndroidAlarmController

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Alarm started", Toast.LENGTH_LONG).show()
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