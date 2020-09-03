package ru.nikstep.alarm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityMainBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.common.onNavItemSelectedListener
import ru.nikstep.alarm.ui.main.alarms.AlarmItemTouchHelperCallback
import ru.nikstep.alarm.ui.main.alarms.AlarmListAdapter
import ru.nikstep.alarm.ui.notifications.NotificationsActivity
import ru.nikstep.alarm.util.startActivityWithIntent
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        buildNewAlarmButtonListener()

        val listAdapter = AlarmListAdapter(viewModel.getAlarms() as MutableList<Alarm>, onAlarmLineClick)

        buildAlarmList(listAdapter)
        buildSwipeAlarmListener(listAdapter)

        binding.topAppBar.menu.findItem(R.id.notificationsPage).setOnMenuItemClickListener {
            startActivityWithIntent(this, NotificationsActivity::class.java)
            true
        }

        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener(onNavItemSelectedListener(this))
        bottomNavigation.menu.findItem(R.id.alarmPage).isChecked = true
    }

    private fun buildNewAlarmButtonListener() {
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, AlarmActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.open_alarm_activity, R.anim.close_main_activity)
        }
    }

    private fun buildAlarmList(listAdapter: AlarmListAdapter) {
        val alarmList = binding.alarmList
        alarmList.setHasFixedSize(true)
        alarmList.layoutManager = LinearLayoutManager(this)
        alarmList.adapter = listAdapter

        ItemTouchHelper(AlarmItemTouchHelperCallback { viewHolder ->
            listAdapter.removeItem(viewHolder.adapterPosition)
            viewModel.removeAlarm(viewHolder.itemView.tag as Long)
        }).attachToRecyclerView(alarmList)
    }

    private fun buildSwipeAlarmListener(listAdapter: AlarmListAdapter) {
        val mainSwipeContainer = binding.mainSwipeContainer
        mainSwipeContainer.setOnRefreshListener {
            listAdapter.updateItems(viewModel.getAlarms())
            mainSwipeContainer.isRefreshing = false
        }
    }

    private val onAlarmLineClick: (i: Alarm) -> Unit = { alarm ->
        startActivityWithIntent(applicationContext, AlarmActivity::class.java, "alarmId" to alarm.id)
    }

    /**
     * Temporary debug method
     */
    fun nextSong(view: View) {
        val playlist = binding.playlistNameInput.text.toString()
        viewModel.play(playlist)
    }

    override fun initViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViewModel(): MainViewModel = viewModelOf(viewModelProvider)
}
