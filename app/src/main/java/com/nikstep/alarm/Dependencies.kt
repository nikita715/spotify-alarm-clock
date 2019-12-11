package com.nikstep.alarm

import android.content.Context
import com.nikstep.alarm.android.wrapper.AlarmAudioManager
import com.nikstep.alarm.android.wrapper.AlarmManager
import com.nikstep.alarm.android.wrapper.AlarmMusicProperties
import com.nikstep.alarm.database.AlarmDatabase
import com.nikstep.alarm.database.SongDatabase
import com.nikstep.alarm.database.helper.DbHelper
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
        val dbHelper = DbHelper(context)
        val songDatabase = SongDatabase(dbHelper)
        val alarmDatabase = AlarmDatabase(dbHelper)
        val alarmMusicProperties = AlarmMusicProperties(context)
        val audioManager = AlarmAudioManager(context)
        val songService = SongService(context, songDatabase)
        val alarmService = AlarmService(alarmDatabase)
        val alarmManager = AlarmManager(context, alarmMusicProperties, audioManager, songService, alarmService)

        put(dbHelper)
        put(songDatabase)
        put(alarmDatabase)
        put(songService)
        put(alarmService)
        put(alarmMusicProperties)
        put(alarmManager)
        put(audioManager)
    }
}