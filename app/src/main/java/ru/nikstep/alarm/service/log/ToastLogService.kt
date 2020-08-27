package ru.nikstep.alarm.service.log

import android.app.Application
import android.widget.Toast
import javax.inject.Inject

class ToastLogService @Inject constructor(
    private val application: Application
) : LogService {

    override fun showMessage(id: Int, vararg properties: Any) =
        Toast.makeText(application, application.getString(id, *properties), Toast.LENGTH_LONG).show()

}