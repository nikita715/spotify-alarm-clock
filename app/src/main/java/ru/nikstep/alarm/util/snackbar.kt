package ru.nikstep.alarm.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(
    view: View,
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    actionName: String = "OK",
    action: () -> Unit = {}
) {
    Snackbar.make(view, message, duration)
        .setAction(actionName) {
            action.invoke()
        }
        .show()
}