package com.nikstep.alarm.android

import android.app.Application
import com.nikstep.alarm.instantiateDependencies

class AlarmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instantiateDependencies(applicationContext)
    }
}