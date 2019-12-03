package com.nikstep.alarm2.android

import android.app.Application
import com.nikstep.alarm2.instantiateDependencies

class AlarmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instantiateDependencies(applicationContext)
    }
}