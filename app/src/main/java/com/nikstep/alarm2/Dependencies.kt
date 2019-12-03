package com.nikstep.alarm2

import android.content.Context
import com.nikstep.alarm2.database.AlarmDatabase
import com.nikstep.alarm2.database.SongDatabase

object Dependencies {
    private val dependencies = mutableMapOf<Dependency, Any>()

    fun <T> get(dependency: Dependency): T? = dependencies[dependency] as T

    fun put(dependency: Dependency, obj: Any) = dependencies.put(dependency, obj)

    fun remove(dependency: Dependency) = dependencies.remove(dependency)
}

fun instantiateDependencies(context: Context) {
    Dependencies.apply {
        put(Dependency.SONG_DATABASE, SongDatabase(context))
        put(Dependency.ALARM_DATABASE, AlarmDatabase(context))
    }
}

enum class Dependency {
    ALARM_DATABASE,
    SONG_DATABASE,
    ALARM_MEDIA_PLAYER
}