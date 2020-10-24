package ru.nikstep.alarm.ui.alarm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import ru.nikstep.alarm.R
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.databinding.ActivityAlarmBinding
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.main.MainActivity
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.startActivityWithIntent
import ru.nikstep.alarm.util.viewmodel.viewModelOf


class AlarmActivity : BaseActivity<AlarmViewModel, ActivityAlarmBinding>() {

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getPlaylists().observeResult(this, successBlock = { playlists ->
            val adapter: ArrayAdapter<Playlist> = ArrayAdapter<Playlist>(
                applicationContext,
                R.layout.playlist_dropdown_menu_item,
                playlists
            )

            binding.playlistDropdownList.adapter = adapter
            intent.extras?.getLong(ALARM_ID_EXTRA)?.let { alarmId ->
                viewModel.getAlarm(alarmId).observeResult(this, successBlock = { alarm ->
                    if (alarm != null) {
                        binding.timePicker.hour = alarm.hour
                        binding.timePicker.minute = alarm.minute
                        binding.playlistDropdownList.setSelection(playlists.indexOfFirst { it.id == alarm.playlist })
                    } else {
                        binding.buttonRemoveAlarm.visibility = View.GONE
                    }
                })
            }

        })

        binding.timePicker.setIs24HourView(true)

        binding.buttonSetAlarm.setOnClickListener {
            val selectedPlaylist = binding.playlistDropdownList.selectedItem as Playlist?
            if (selectedPlaylist == null) {
                viewModel.warnNoPlaylistSelected()
            } else {
                viewModel.setAlarm(
                    AlarmData(
                        id = intent.extras?.getLong(ALARM_ID_EXTRA),
                        hour = binding.timePicker.hour,
                        minute = binding.timePicker.minute,
                        playlist = selectedPlaylist.id
                    )
                ).observeResult(this, successBlock = {
                    runOnUiThread {
                        Toast.makeText(this, "Set alarm at ${it.hour}:${it.minute}", Toast.LENGTH_LONG).show()
                    }
                })
                returnToMainActivity()
            }
        }

        binding.buttonRemoveAlarm.setOnClickListener {
            intent.extras?.getLong(ALARM_ID_EXTRA)?.let { viewModel.removeAlarm(it) }
            returnToMainActivity()
            overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
        }
    }

    private fun returnToMainActivity() = startActivityWithIntent(applicationContext, MainActivity::class.java)

    override fun initViewBinding(): ActivityAlarmBinding = ActivityAlarmBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmViewModel = viewModelOf(viewModelProvider)
}