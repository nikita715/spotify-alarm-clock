package ru.nikstep.alarm.service.alarm

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.nikstep.alarm.AlarmApp
import javax.inject.Inject

class StopAlarmService : IntentService(StopAlarmService::class.java.simpleName) {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onHandleIntent(intent: Intent?) {
        (applicationContext as AlarmApp).androidInjector.inject(this)
        if (intent?.action == "Close") {
            alarmController.stopAlarm()
            Log.i("StopMusicService", "Stopped music")
        }
    }
}