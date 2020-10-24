package ru.nikstep.alarm.ui.alarmlog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nikstep.alarm.databinding.ActivityAlarmLogBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.data.observeResult
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
        viewModel.getAlarmLogs().observeResult(this, loadingBlock = {
            binding.mainContainer.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }, successBlock = { alarmLogs ->
            val alarmLogListAdapter = AlarmLogListAdapter(alarmLogs) {}
            playlistListView.adapter = alarmLogListAdapter
            buildSwipeAlarmListener(alarmLogListAdapter)
            binding.progressBar.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE
        })
    }

    private fun buildSwipeAlarmListener(listAdapter: AlarmLogListAdapter) {
        val mainContainer = binding.mainContainer
        mainContainer.setOnRefreshListener {
            viewModel.getAlarmLogs().observeResult(this, successBlock = { alarmLogs ->
                listAdapter.updateItems(alarmLogs)
                mainContainer.isRefreshing = false
            }, errorBlock = {
                mainContainer.isRefreshing = false
            })
        }
    }

    override fun initViewBinding(): ActivityAlarmLogBinding = ActivityAlarmLogBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmLogViewModel = viewModelOf(viewModelProvider)
}