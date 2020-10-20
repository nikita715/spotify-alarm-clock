package ru.nikstep.alarm.service.alarm.android

import android.app.Service
import android.content.Intent
import android.os.IBinder
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import javax.inject.Inject

class AlarmService : Service() {
    @Inject
    lateinit var alarmController: AlarmController

    @Inject
    lateinit var notificationService: NotificationService

    override fun onCreate() {
        super.onCreate()
        (applicationContext as AlarmApp).androidInjector.inject(this)
        startForeground(1, notificationService.buildAlarmNotification())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarmController.startAlarm(intent.getLongExtra(ALARM_ID_EXTRA, -1L))
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmController.stopAlarm()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}