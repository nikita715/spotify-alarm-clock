package ru.nikstep.alarm.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.Serializable

fun Context.startActivityWithIntent(
    activity: Class<out Activity>,
    vararg extras: Pair<String, Serializable>
) {
    val intent = Intent()
    intent.setClass(this, activity)
    extras.forEach {
        intent.putExtra(it.first, it.second)
    }
    startActivity(intent)
}