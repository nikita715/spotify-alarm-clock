package ru.nikstep.alarm.ui.trackselect

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.nikstep.alarm.databinding.ActivityTrackSelectBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class TrackSelectActivity : BaseActivity<TrackSelectViewModel, ActivityTrackSelectBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra("PLAYLIST_ID")?.let { playlistId ->
            downloadPlaylistTracks(playlistId, binding.trackList)
        }
    }

    private fun downloadPlaylistTracks(playlistId: String, trackListView: RecyclerView) {
        viewModel.getPlaylistTracks(playlistId).observeResult(this,
            loadingBlock = this::showRefresh,
            successBlock = { tracks ->
                trackListView.setHasFixedSize(true)
                trackListView.layoutManager = LinearLayoutManager(this)
                val alarmLogListAdapter = TrackSelectListAdapter(tracks)
                trackListView.adapter = alarmLogListAdapter
                buildRefreshTracksListener(playlistId, alarmLogListAdapter)
                showContent()
            }, errorBlock = {
                Snackbar.make(binding.root, "Error at the update of tracks", Snackbar.LENGTH_LONG).show()
            })
    }

    private fun buildRefreshTracksListener(playlistId: String, listAdapter: TrackSelectListAdapter) {
        val mainContainer = binding.mainContainer
        mainContainer.setOnRefreshListener {
            viewModel.getPlaylistTracks(playlistId).observeResult(this,
                successBlock = { tracks ->
                    listAdapter.updateItems(tracks)
                }, errorBlock = {
                    Snackbar.make(binding.root, "Error at the update of tracks", Snackbar.LENGTH_LONG).show()
                })
            mainContainer.isRefreshing = false
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

    override fun initViewBinding(): ActivityTrackSelectBinding = ActivityTrackSelectBinding.inflate(layoutInflater)

    override fun initViewModel(): TrackSelectViewModel = viewModelOf(viewModelProvider)
}