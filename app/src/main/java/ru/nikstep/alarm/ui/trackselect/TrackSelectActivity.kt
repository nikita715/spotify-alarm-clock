package ru.nikstep.alarm.ui.trackselect

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nikstep.alarm.databinding.ActivityTrackSelectBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class TrackSelectActivity : BaseActivity<TrackSelectViewModel, ActivityTrackSelectBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intent.getStringExtra("PLAYLIST_ID")?.let { playlistId ->
            val trackListView = binding.trackList
            trackListView.setHasFixedSize(true)
            trackListView.layoutManager = LinearLayoutManager(this)
            val alarmLogListAdapter = TrackSelectListAdapter()
            trackListView.adapter = alarmLogListAdapter
            downloadPlaylistTracks(playlistId, alarmLogListAdapter)
            buildSwipeAlarmListener(playlistId, alarmLogListAdapter)
        }
    }

    private fun buildSwipeAlarmListener(playlistId: String, listAdapter: TrackSelectListAdapter) {
        val mainSwipeContainer = binding.mainSwipeContainer
        mainSwipeContainer.setOnRefreshListener {
            listAdapter.removeAllItems()
            downloadPlaylistTracks(playlistId, listAdapter)
            mainSwipeContainer.isRefreshing = false
        }
    }

    private fun downloadPlaylistTracks(playlistId: String, listAdapter: TrackSelectListAdapter) {
        viewModel.getPlaylistTracks(playlistId).observeResult(this, successBlock = { tracks ->
            listAdapter.updateItems(tracks)
        })
    }

    override fun initViewBinding(): ActivityTrackSelectBinding = ActivityTrackSelectBinding.inflate(layoutInflater)

    override fun initViewModel(): TrackSelectViewModel = viewModelOf(viewModelProvider)
}