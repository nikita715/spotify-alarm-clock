package ru.nikstep.alarm.service.alarm.android

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Alarm started", Toast.LENGTH_LONG).show()
        (applicationContext as AlarmApp).androidInjector.inject(this)
        notificationService.notify("")
        alarmController.startAlarm(intent.getLongExtra(ALARM_ID_EXTRA, -1L))
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmController.stopAlarm()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}