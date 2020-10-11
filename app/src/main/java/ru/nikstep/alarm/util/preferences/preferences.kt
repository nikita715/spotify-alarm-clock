package ru.nikstep.alarm.util.preferences

import android.content.Context
import android.util.Log
import ru.nikstep.alarm.R

inline fun <reified T : Any> Context.getAppPreference(name: Int): T? {
    val preferences = getSharedPreferences(
        getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    return when (T::class) {
        Boolean::class -> preferences.getBoolean(getString(name), false) as T
        String::class -> preferences.getString(getString(name), null) as T?
        Int::class -> preferences.getInt(getString(name), -1) as T
        Long::class -> preferences.getLong(getString(name), -1L) as T
        Float::class -> preferences.getFloat(getString(name), -1F) as T
        Set::class -> preferences.getStringSet(getString(name), emptySet()) as T
        else -> {
            Log.e("", "Wrong type of property")
            null
        }
    }
}

fun <T : Any> Context.setAppPreference(name: Int, value: T) {
    val editor = getSharedPreferences(
        getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    ).edit()
    when (value::class) {
        Boolean::class -> editor.putBoolean(getString(name), value as Boolean)
        String::class -> editor.putString(getString(name), value as String)
        Int::class -> editor.putInt(getString(name), value as Int)
        Long::class -> editor.putLong(getString(name), value as Long)
        Float::class -> editor.putFloat(getString(name), value as Float)
        Set::class -> editor.putStringSet(getString(name), value as Set<String>)
        else -> Log.e("", "Wrong type of property")
    }
    editor.apply()
}