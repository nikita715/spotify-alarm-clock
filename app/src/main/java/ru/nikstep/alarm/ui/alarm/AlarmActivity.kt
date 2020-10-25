package ru.nikstep.alarm.ui.alarm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

        val alarmId = intent.extras?.getLong(ALARM_ID_EXTRA)
        viewModel.getPlaylists().observeResult(this, loadingBlock = {
            binding.mainContainer.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }, successBlock = { playlists ->
            val adapter: ArrayAdapter<Playlist> = ArrayAdapter<Playlist>(
                applicationContext,
                R.layout.playlist_dropdown_menu_item,
                playlists
            )

            binding.playlistDropdownList.adapter = adapter

            if (alarmId != null) {
                viewModel.getAlarm(alarmId).observeResult(this, successBlock = { alarm ->
                    if (alarm != null) {
                        binding.timePicker.hour = alarm.hour
                        binding.timePicker.minute = alarm.minute
                        binding.playlistDropdownList.setSelection(playlists.indexOfFirst { it.id == alarm.playlist })
                    } else {
                        binding.buttonRemoveAlarm.visibility = View.GONE
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.mainContainer.visibility = View.VISIBLE
                })
            } else {
                binding.progressBar.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
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
                        id = alarmId,
                        hour = binding.timePicker.hour,
                        minute = binding.timePicker.minute,
                        playlist = selectedPlaylist.id
                    )
                ).observeResult(this, successBlock = { alarm ->
                    startActivityWithIntent(
                        applicationContext, MainActivity::class.java,
                        "CREATED_ALARM" to true,
                        "CREATED_ALARM_TIME" to "${alarm.hour}:${alarm.minute}"
                    )
                    overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
                })
            }
        }

        binding.buttonRemoveAlarm.setOnClickListener {
            alarmId?.let {
                viewModel.removeAlarm(it).observeResult(this, successBlock = {
                    startActivityWithIntent(applicationContext, MainActivity::class.java, "REMOVED_ALARM" to true)
                    overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
                })
            }
        }
    }

    private fun returnToMainActivity() = startActivityWithIntent(applicationContext, MainActivity::class.java)

    override fun initViewBinding(): ActivityAlarmBinding = ActivityAlarmBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmViewModel = viewModelOf(viewModelProvider)
}