package com.nikstep.alarm2

import android.app.Application

class AlarmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instantiateDependencies(applicationContext)
    }
}