package ru.nikstep.alarm.service.log

import android.app.Application
import android.os.Looper
import android.widget.Toast
import javax.inject.Inject

class ToastLogService @Inject constructor(
    private val application: Application
) : LogService {

    override fun showMessage(id: Int, vararg properties: Any) {
        Looper.myLooper() ?: Looper.prepare()
        Toast.makeText(application, application.getString(id, *properties), Toast.LENGTH_LONG).show()
    }

}