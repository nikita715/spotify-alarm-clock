package ru.nikstep.alarm.service.alarm.android

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.util.data.emitLiveData
import ru.nikstep.alarm.util.data.observeResult
import javax.inject.Inject

class StopAlarmService : LifecycleService() {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        (applicationContext as AlarmApp).androidInjector.inject(this)
        when (intent?.action) {
            CLOSE_ALARM_ACTION -> {
                emitLiveData {
                    alarmController.stopAlarm()
                }.observeResult(this, successBlock = {
                    Log.i("StopMusicService", "Stopped the music")
                })
                stopService(Intent(applicationContext, AlarmService::class.java))
            }
            DISABLE_NEXT_ALARM_ACTION -> {
                emitLiveData {
                    alarmController.disableNextActivationOfAlarm(intent.getLongExtra(ALARM_ID_EXTRA, -1L))
                }.observeResult(this, successBlock = {
                    Log.i("StopMusicService", "Disabled next alarm")
                })
            }
        }
        return START_NOT_STICKY
    }

    companion object {
        const val CLOSE_ALARM_ACTION = "Close"
        const val DISABLE_NEXT_ALARM_ACTION = "DisableNextAlarm"
    }
}