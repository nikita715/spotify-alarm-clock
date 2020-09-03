package ru.nikstep.alarm.ui.playlists

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityPlaylistsBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class PlaylistsActivity : BaseActivity<PlaylistsViewModel, ActivityPlaylistsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.alarmPage -> Log.i("MainActivity", "alarm reselected")
                R.id.playlistsPage -> Log.i("MainActivity", "playlists reselected")
            }
        }
        bottomNavigation.menu.findItem(R.id.playlistsPage).isChecked = true
    }

    override fun initViewBinding(): ActivityPlaylistsBinding = ActivityPlaylistsBinding.inflate(layoutInflater)

    override fun initViewModel(): PlaylistsViewModel = viewModelOf(viewModelProvider)
}