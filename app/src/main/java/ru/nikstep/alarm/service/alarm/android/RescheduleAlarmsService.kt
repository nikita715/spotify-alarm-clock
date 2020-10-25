package ru.nikstep.alarm.service.alarm.android

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.util.data.emitLiveData
import ru.nikstep.alarm.util.data.observeResult
import javax.inject.Inject

class RescheduleAlarmsService : LifecycleService() {
    @Inject
    lateinit var alarmController: AlarmController

    @Inject
    lateinit var alarmDataService: AlarmDataService

    @Inject
    lateinit var notificationService: NotificationService

    override fun onCreate() {
        super.onCreate()
        startForeground(1, notificationService.buildAlarmNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        (applicationContext as AlarmApp).androidInjector.inject(this)

        emitLiveData {
            alarmDataService.findAll().map { alarm ->
                alarmController.setAlarm(alarm)
            }
        }.observeResult(this, successBlock = { alarms ->
            alarms.forEach { alarm ->
                alarm?.also {
                    Log.i(
                        "ReschAlarmsService", "Alarm with id ${alarm.id}" +
                                " scheduled at ${alarm.hour}^${alarm.minute}"
                    )
                } ?: Log.e("ReschAlarmsService", "Alarm wasn't saved")
            }
        })
        return START_STICKY
    }
}