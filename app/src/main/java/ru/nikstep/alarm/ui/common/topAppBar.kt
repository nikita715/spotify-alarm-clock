package ru.nikstep.alarm.ui.common

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import ru.nikstep.alarm.R
import ru.nikstep.alarm.ui.alarmlog.AlarmLogActivity
import ru.nikstep.alarm.ui.notifications.NotificationsActivity
import ru.nikstep.alarm.ui.settings.SettingsActivity
import ru.nikstep.alarm.util.preferences.getAppPreference
import ru.nikstep.alarm.util.startActivityWithIntent

fun AppCompatActivity.buildTopAppBar(toolbar: MaterialToolbar): Unit = toolbar.menu.let { menu ->
    menu.findItem(R.id.notificationsPage).also { notificationsMenuItem ->
        when (getAppPreference<Boolean>(R.string.saved_notifications_key)) {
            true -> Unit
            false -> notificationsMenuItem.setIcon(R.drawable.baseline_notifications_none_white_24dp)
        }
        notificationsMenuItem.setOnMenuItemClickListener {
            startActivityWithIntent(NotificationsActivity::class.java)
            true
        }
    }
    menu.findItem(R.id.alarmLogPage).setOnMenuItemClickListener {
        startActivityWithIntent(AlarmLogActivity::class.java)
        true
    }
    menu.findItem(R.id.settingsPage).setOnMenuItemClickListener {
        startActivityWithIntent(SettingsActivity::class.java)
        true
    }
}