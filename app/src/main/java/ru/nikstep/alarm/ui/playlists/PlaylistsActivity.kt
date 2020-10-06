package ru.nikstep.alarm.ui.playlists

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityPlaylistsBinding
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.ui.notifications.NotificationsActivity
import ru.nikstep.alarm.util.data.Status
import ru.nikstep.alarm.util.startActivityWithIntent
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class PlaylistsActivity : BaseActivity<PlaylistsViewModel, ActivityPlaylistsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.menu.findItem(R.id.playlistsPage).isChecked = true

        val notificationsMenuItem: MenuItem = binding.topAppBar.menu.findItem(R.id.notificationsPage)
        val hasNotifications = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        ).getBoolean(getString(R.string.saved_notifications_key), false)
        when (hasNotifications) {
            true -> Unit
            false -> notificationsMenuItem.setIcon(R.drawable.baseline_notifications_none_white_24dp)
        }
        notificationsMenuItem.setOnMenuItemClickListener {
            startActivityWithIntent(this, NotificationsActivity::class.java)
            true
        }

        viewModel.getPlaylists()
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            val playlistListView = binding.playlistList
                            playlistListView.setHasFixedSize(true)
                            playlistListView.layoutManager = LinearLayoutManager(this)
                            playlistListView.adapter =
                                resource.data?.let { list -> PlaylistListAdapter(list as MutableList<Playlist>) {} }
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
    }

    override fun initViewBinding(): ActivityPlaylistsBinding = ActivityPlaylistsBinding.inflate(layoutInflater)

    override fun initViewModel(): PlaylistsViewModel = viewModelOf(viewModelProvider)
}