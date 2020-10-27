package ru.nikstep.alarm.ui.playlists

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityPlaylistsBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.common.buildBottomNavigationBar
import ru.nikstep.alarm.ui.common.buildTopAppBar
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class PlaylistsActivity : BaseActivity<PlaylistsViewModel, ActivityPlaylistsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showPlaylists(binding.mainContainer, binding.playlistList)

        buildTopAppBar(binding.topAppBar)
        buildBottomNavigationBar(binding.bottomNavigation, R.id.playlistsPage)
    }

    private fun showPlaylists(container: SwipeRefreshLayout, playlistsView: RecyclerView) {
        viewModel.getPlaylists().observeResult(this, loadingBlock = {
            showRefresh()
        }, successBlock = { playlists ->
            playlistsView.setHasFixedSize(true)
            playlistsView.layoutManager = LinearLayoutManager(this)
            val playlistListAdapter = PlaylistListAdapter(playlists)
            playlistsView.adapter = playlistListAdapter
            buildSwipePlaylistsListener(container, playlistsView)
            showContent()
        })
    }

    private fun buildSwipePlaylistsListener(container: SwipeRefreshLayout, playlistsView: RecyclerView) {
        container.setOnRefreshListener {
            val listAdapter = playlistsView.adapter as PlaylistListAdapter
            viewModel.downloadPlaylists().observeResult(this, successBlock = { downloadedPlaylists ->
                viewModel.savePlaylists(downloadedPlaylists).observeResult(this, successBlock = { savedPlaylists ->
                    listAdapter.updateItems(savedPlaylists)
                    container.isRefreshing = false
                })
            }, errorBlock = {
                container.isRefreshing = false
            })
        }
    }

    private fun showRefresh() {
        binding.mainContainer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.mainContainer.visibility = View.VISIBLE
    }

    override fun initViewBinding(): ActivityPlaylistsBinding = ActivityPlaylistsBinding.inflate(layoutInflater)

    override fun initViewModel(): PlaylistsViewModel = viewModelOf(viewModelProvider)
}