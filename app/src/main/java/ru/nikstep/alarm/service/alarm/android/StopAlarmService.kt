package ru.nikstep.alarm.service.alarm.android

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.service.alarm.AlarmController
import javax.inject.Inject

class StopAlarmService : IntentService(StopAlarmService::class.java.simpleName) {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onHandleIntent(intent: Intent?) {
        (applicationContext as AlarmApp).androidInjector.inject(this)
        if (intent?.action == CLOSE_ALARM_ACTION) {
            alarmController.stopAlarm()
            Log.i("StopMusicService", "Stopped music")
        }
        stopService(Intent(applicationContext, AlarmService::class.java))
    }

    companion object {
        const val CLOSE_ALARM_ACTION = "Close"
    }
}