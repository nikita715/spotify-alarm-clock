package ru.nikstep.alarm.service.alarm.android

import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.util.data.emitLiveData
import ru.nikstep.alarm.util.data.observeResult
import javax.inject.Inject

class AlarmService : LifecycleService() {
    @Inject
    lateinit var alarmController: AlarmController

    @Inject
    lateinit var notificationService: NotificationService

    override fun onCreate() {
        super.onCreate()
        (applicationContext as AlarmApp).androidInjector.inject(this)
        startForeground(1, notificationService.buildAlarmNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmId = intent?.getLongExtra(ALARM_ID_EXTRA, -1L) ?: -1L
        emitLiveData {
            alarmController.buildSpotifyMusicData(alarmId)
        }.observeResult(this, { musicData ->
            if (musicData != null) {
                alarmController.startAlarm(musicData)
            } else {
                Log.i("AlarmService", "Alarm not found")
            }
        })
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        emitLiveData {
            alarmController.stopAlarm()
        }.observeResult(this, {
            Log.i("AlarmService", "Alarm stopped")
        })
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}