package ru.nikstep.alarm.service.alarm.android

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.AlarmApp
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.service.alarm.AlarmController
import ru.nikstep.alarm.service.data.AlarmDataService
import ru.nikstep.alarm.util.data.Resource
import ru.nikstep.alarm.util.data.Status
import javax.inject.Inject

class RescheduleAlarmsService : LifecycleService() {
    @Inject
    lateinit var alarmController: AlarmController

    @Inject
    lateinit var alarmDataService: AlarmDataService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        (applicationContext as AlarmApp).androidInjector.inject(this)

        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = alarmDataService.findAll()))
            } catch (e: Exception) {
                Log.e("ReschedAlarmsService", e.message, e)
                emit(Resource.error(data = e, message = e.message ?: "Error Occurred!"))
            }
        }.observe(this, {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        resource.data?.let { presentData ->
                            val alarms = presentData as List<Alarm>
                            alarms.forEach { alarm ->
                                alarmController.setAlarm(
                                    AlarmData(
                                        alarm.id,
                                        alarm.hour,
                                        alarm.minute,
                                        alarm.playlist
                                    )
                                )
                            }
                        }
                    }
                    Status.ERROR -> {
                        Log.e("ReschedAlarmsService", it.message, it.data as Throwable)
                    }
                }
            }
        })
        return START_STICKY
    }
}