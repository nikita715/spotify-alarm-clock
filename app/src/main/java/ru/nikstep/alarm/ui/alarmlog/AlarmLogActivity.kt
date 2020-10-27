package ru.nikstep.alarm.ui.alarmlog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.nikstep.alarm.databinding.ActivityAlarmLogBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class AlarmLogActivity : BaseActivity<AlarmLogViewModel, ActivityAlarmLogBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showAlarmLogs(binding.mainContainer, binding.alarmLogList)
    }

    private fun showAlarmLogs(container: SwipeRefreshLayout, logListView: RecyclerView) {
        logListView.setHasFixedSize(true)
        logListView.layoutManager = LinearLayoutManager(this)
        viewModel.getAlarmLogs().observeResult(this, loadingBlock = {
            showRefresh()
        }, successBlock = { alarmLogs ->
            val alarmLogListAdapter = AlarmLogListAdapter(alarmLogs) {}
            logListView.adapter = alarmLogListAdapter
            buildSwipeAlarmListener(container, alarmLogListAdapter)
            showContent()
        })
    }

    private fun buildSwipeAlarmListener(container: SwipeRefreshLayout, listAdapter: AlarmLogListAdapter) =
        container.setOnRefreshListener {
            viewModel.getAlarmLogs().observeResult(this, successBlock = { alarmLogs ->
                listAdapter.updateItems(alarmLogs)
                container.isRefreshing = false
            }, errorBlock = {
                container.isRefreshing = false
            })
        }

    private fun showRefresh() {
        binding.mainContainer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.mainContainer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun initViewBinding(): ActivityAlarmLogBinding = ActivityAlarmLogBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmLogViewModel = viewModelOf(viewModelProvider)
}