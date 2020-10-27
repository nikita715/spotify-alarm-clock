package ru.nikstep.alarm.ui.common

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nikstep.alarm.R
import ru.nikstep.alarm.ui.main.MainActivity
import ru.nikstep.alarm.ui.playlists.PlaylistsActivity
import ru.nikstep.alarm.util.startActivityWithIntent

fun AppCompatActivity.buildBottomNavigationBar(bottomNavigationView: BottomNavigationView, activePage: Int) =
    bottomNavigationView.let {
        it.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        it.menu.findItem(activePage).isChecked = true
    }

fun AppCompatActivity.onNavItemSelectedListener(activity: Activity): BottomNavigationView.OnNavigationItemSelectedListener =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val result = when (item.itemId) {
            R.id.alarmPage -> {
                Log.i("MainActivity", "alarm selected")
                startActivityWithIntent(MainActivity::class.java)
                true
            }
            R.id.playlistsPage -> {
                Log.i("MainActivity", "playlists selected")
                startActivityWithIntent(PlaylistsActivity::class.java)
                true
            }
            else -> false
        }
        activity.overridePendingTransition(0, 0)
        result
    }