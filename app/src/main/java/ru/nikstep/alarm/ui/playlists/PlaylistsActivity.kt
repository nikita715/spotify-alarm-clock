package ru.nikstep.alarm.ui.playlists

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityPlaylistsBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.base.buildTopAppBar
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class PlaylistsActivity : BaseActivity<PlaylistsViewModel, ActivityPlaylistsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.menu.findItem(R.id.playlistsPage).isChecked = true

        buildTopAppBar(binding.topAppBar)

        val playlistListView = binding.playlistList
        playlistListView.setHasFixedSize(true)
        playlistListView.layoutManager = LinearLayoutManager(this)
        val playlistListAdapter = PlaylistListAdapter()
        playlistListView.adapter = playlistListAdapter
        showPlaylists(playlistListAdapter)

        buildSwipePlaylistsListener()
    }

    private fun showPlaylists(adapter: PlaylistListAdapter) {
        viewModel.getPlaylists().observeResult(this, loadingBlock = {
            binding.mainContainer.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }, successBlock = { playlists ->
            adapter.updateItems(playlists)
            binding.progressBar.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE
        })
    }

    private fun buildSwipePlaylistsListener() {
        binding.mainContainer.setOnRefreshListener {
            val playlistListAdapter = binding.playlistList.adapter as PlaylistListAdapter
            viewModel.downloadPlaylists().observeResult(this, successBlock = { downloadedPlaylists ->
                viewModel.savePlaylists(downloadedPlaylists).observeResult(this, successBlock = { savedPlaylists ->
                    playlistListAdapter.updateItems(savedPlaylists)
                    binding.mainContainer.isRefreshing = false
                })
            }, errorBlock = {
                binding.mainContainer.isRefreshing = false
            })
        }
    }

    override fun initViewBinding(): ActivityPlaylistsBinding = ActivityPlaylistsBinding.inflate(layoutInflater)

    override fun initViewModel(): PlaylistsViewModel = viewModelOf(viewModelProvider)
}