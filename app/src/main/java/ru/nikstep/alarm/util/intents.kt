package ru.nikstep.alarm.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.io.Serializable

fun buildIntent(context: Context, activity: Class<out Activity>, vararg extras: Pair<String, Serializable>) {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setClass(context, activity)
    extras.forEach {
        intent.putExtra(it.first, it.second)
    }
    context.startActivity(intent)
}