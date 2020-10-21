package ru.nikstep.alarm.ui.alarmlog

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nikstep.alarm.databinding.ActivityAlarmLogBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class AlarmLogActivity : BaseActivity<AlarmLogViewModel, ActivityAlarmLogBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showAlarmLogs()
    }

    private fun showAlarmLogs() {
        val playlistListView = binding.alarmLogList
        playlistListView.setHasFixedSize(true)
        playlistListView.layoutManager = LinearLayoutManager(this)
        val alarmLogListAdapter = AlarmLogListAdapter(viewModel.getAlarmLogs()) {}
        playlistListView.adapter = alarmLogListAdapter
        buildSwipeAlarmListener(alarmLogListAdapter)
    }

    private fun buildSwipeAlarmListener(listAdapter: AlarmLogListAdapter) {
        val mainSwipeContainer = binding.mainSwipeContainer
        mainSwipeContainer.setOnRefreshListener {
            listAdapter.updateItems(viewModel.getAlarmLogs())
            mainSwipeContainer.isRefreshing = false
        }
    }

    override fun initViewBinding(): ActivityAlarmLogBinding = ActivityAlarmLogBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmLogViewModel = viewModelOf(viewModelProvider)
}