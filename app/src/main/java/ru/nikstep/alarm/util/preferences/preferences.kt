package ru.nikstep.alarm.util.preferences

import android.content.Context
import ru.nikstep.alarm.R

inline fun <reified T : Any> Context.getAppPreference(name: Int): T {
    val sharedPreferences = getSharedPreferences(
        getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    return when (T::class) {
        Boolean::class -> sharedPreferences.getBoolean(getString(name), false) as T
        String::class -> sharedPreferences.getString(getString(name), null) as T
        Int::class -> sharedPreferences.getInt(getString(name), -1) as T
        else -> throw RuntimeException()
    }
}