package ru.nikstep.alarm.service.alarm.android.reminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.notification.NotificationService
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.Status
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class AlarmReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var alarmController: AlarmController

    override fun onReceive(context: Context, intent: Intent) {
        if (context is ContextWrapper) {
            (context.baseContext as AlarmApp).androidInjector.inject(this)
        } else {
            (context as AlarmApp).androidInjector.inject(this)
        }
        startAlarmService(context, intent)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra(ALARM_ID_EXTRA, -1L)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val liveData = emitLiveData {
            alarmController.getAlarm(alarmId)
        }
        val observer = AlarmNotificationObserver(liveData, notificationManager, notificationService)
        liveData.observeForever(observer)
    }

    class AlarmNotificationObserver(
        private val liveData: LiveData<Result<Alarm?>>,
        private val notificationManager: NotificationManager,
        private val notificationService: NotificationService
    ) : Observer<Result<Alarm?>> {
        override fun onChanged(t: Result<Alarm?>?) {
            when (t?.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    t.data?.let { alarm ->
                        notificationManager.notify(
                            1,
                            notificationService.buildAlarmReminderNotification(alarm.id, alarm.getTimeAsString())
                        )
                    }
                    Log.i("AlarmRemRec", "Notification is sent")
                    liveData.removeObserver(this)
                }
                Status.ERROR -> {
                    Log.e("AlarmRemRec", t.exception?.message, t.exception)
                    liveData.removeObserver(this)
                }
            }
        }

    }
}