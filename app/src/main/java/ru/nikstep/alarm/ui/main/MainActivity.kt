package ru.nikstep.alarm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.databinding.ActivityMainBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.main.alarms.AlarmItemTouchHelperCallback
import ru.nikstep.alarm.ui.main.alarms.AlarmListAdapter
import ru.nikstep.alarm.util.viewmodel.viewModelOf


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, AlarmActivity::class.java)
            startActivity(intent)
        }

        val alarmList = binding.alarmList
        alarmList.setHasFixedSize(true)
        alarmList.layoutManager = LinearLayoutManager(this)
        alarmList.adapter = AlarmListAdapter(viewModel.getAlarms() as MutableList<Alarm>, onItemClick)

        val itemTouchHelper =
            ItemTouchHelper(AlarmItemTouchHelperCallback(object : AlarmItemTouchHelperCallback.OnSwipeListener {
                override fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder) {
                    (alarmList.adapter as AlarmListAdapter).removeItem(viewHolder.adapterPosition)
                    viewModel.removeAlarm(viewHolder.itemView.tag as Long)
                }
            }))
        itemTouchHelper.attachToRecyclerView(alarmList)
    }

    private val onItemClick: (i: Alarm) -> Unit = {
        val intent = Intent()
        intent.setClass(this, AlarmActivity::class.java)
        intent.putExtra("alarmId", it.id)
        startActivity(intent)
    }

    fun nextSong(view: View) {
        val playlist = binding.playlistNameInput.text.toString()
        viewModel.play(playlist)
    }

    override fun initViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViewModel(): MainViewModel = viewModelOf(viewModelProvider)
}
