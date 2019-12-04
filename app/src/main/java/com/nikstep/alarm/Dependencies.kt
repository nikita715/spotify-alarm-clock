package com.nikstep.alarm

import android.content.Context
import com.nikstep.alarm.android.wrapper.AlarmAudioManager
import com.nikstep.alarm.android.wrapper.AlarmManager
import com.nikstep.alarm.android.wrapper.AlarmMusicProperties
import com.nikstep.alarm.service.AlarmService
import com.nikstep.alarm.service.SongService

object Dependencies {
    private val dependencies = mutableMapOf<Class<out Any>, Any>()

    fun <T> get(clazz: Class<T>): T = dependencies[clazz] as T

    fun <T> getOrNull(clazz: Class<T>): T? = dependencies[clazz] as T

    fun put(obj: Any) = dependencies.put(obj::class.java, obj)

    fun <T> remove(clazz: Class<T>) = dependencies.remove(clazz)
}

fun instantiateDependencies(context: Context) {
    Dependencies.apply {
        put(SongService(context))
        put(AlarmService(context))
        put(AlarmMusicProperties(context))
        put(AlarmManager(context))
        put(AlarmAudioManager(context))
    }
}