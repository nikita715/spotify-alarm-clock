package ru.nikstep.alarm.ui.alarm

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityAlarmBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.model.type.AlarmChangeType
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.main.MainActivity
import ru.nikstep.alarm.ui.main.MainActivity.Companion.ALARM_ID_EXTRA
import ru.nikstep.alarm.util.data.observeResult
import ru.nikstep.alarm.util.showSnackbar
import ru.nikstep.alarm.util.startActivityWithIntent
import ru.nikstep.alarm.util.viewmodel.viewModelOf


class AlarmActivity : BaseActivity<AlarmViewModel, ActivityAlarmBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val alarmId = intent.extras?.getLong(ALARM_ID_EXTRA)

        showPlaylists(alarmId)
        buildSetAlarmListener(alarmId)
        buildRemoveAlarmListener(alarmId)

        binding.timePicker.setIs24HourView(true)
    }

    private fun buildSetAlarmListener(alarmId: Long?) = binding.buttonSetAlarm.setOnClickListener {
        val selectedPlaylist = binding.playlistDropdownList.selectedItem as Playlist?
        if (selectedPlaylist == null) {
            showSnackbar(binding.root, "You have to select a playlist")
        } else {
            viewModel.setAlarm(
                Alarm(
                    id = alarmId ?: 0,
                    hour = binding.timePicker.hour,
                    minute = binding.timePicker.minute,
                    playlist = selectedPlaylist.id
                )
            ).observeResult(this, successBlock = { alarm ->
                alarm?.also {
                    startActivityWithIntent(
                        MainActivity::class.java,
                        AlarmChangeType.EXTRA_NAME to
                                (if (alarmId == null) AlarmChangeType.CREATE else AlarmChangeType.UPDATE),
                        AlarmChangeType.TIME_EXTRA_NAME to alarm.getTimeAsString()
                    )
                    overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
                } ?: showSnackbar(binding.root, "Alarm wasn't created")
            })
        }
    }

    private fun buildRemoveAlarmListener(alarmId: Long?) = binding.buttonRemoveAlarm.setOnClickListener {
        if (alarmId != null) {
            viewModel.removeAlarm(alarmId).observeResult(this, successBlock = {
                startActivityWithIntent(
                    MainActivity::class.java,
                    AlarmChangeType.EXTRA_NAME to AlarmChangeType.DELETE
                )
                overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
            })
        }
    }

    private fun showPlaylists(alarmId: Long?) {
        viewModel.getPlaylists().observeResult(this, loadingBlock = {
            showRefresh()
        }, successBlock = { playlists ->
            binding.playlistDropdownList.adapter = ArrayAdapter(
                applicationContext,
                R.layout.playlist_dropdown_menu_item,
                playlists
            )

            if (alarmId != null) {
                viewModel.getAlarm(alarmId).observeResult(this, successBlock = { alarm ->
                    if (alarm != null) {
                        binding.timePicker.hour = alarm.hour
                        binding.timePicker.minute = alarm.minute
                        binding.playlistDropdownList.setSelection(playlists.indexOfFirst { it.id == alarm.playlist })
                    } else {
                        binding.buttonRemoveAlarm.visibility = View.GONE
                    }
                    showContent()
                })
            } else {
                showContent()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
    }

    private fun showRefresh() {
        binding.mainContainer.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.mainContainer.visibility = View.VISIBLE
    }

    override fun initViewBinding(): ActivityAlarmBinding = ActivityAlarmBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmViewModel = viewModelOf(viewModelProvider)
}