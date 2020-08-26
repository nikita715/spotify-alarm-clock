package ru.nikstep.alarm.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import ru.nikstep.alarm.databinding.ActivityAlarmBinding
import ru.nikstep.alarm.service.AlarmData
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.main.MainActivity
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class AlarmActivity : BaseActivity<AlarmViewModel, ActivityAlarmBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.extras?.getLong("alarmId")
            ?.let(viewModel::getAlarm)
            ?.also { alarm ->
                binding.timePicker.hour = alarm.hour
                binding.timePicker.minute = alarm.minute
                binding.playlistNameInput.setText(alarm.playlist)
            }
    }

    fun setAlarm(view: View) {
        viewModel.setAlarm(
            AlarmData(
                id = intent.extras?.getLong("alarmId"),
                hour = binding.timePicker.hour,
                minute = binding.timePicker.minute,
                playlist = binding.playlistNameInput.text.toString()
            )
        )
        returnToMainActivity()
    }

    fun removeAlarm(view: View) {
        intent.extras?.getLong("id")?.let { viewModel.removeAlarm(it) }
        returnToMainActivity()
    }

    private fun returnToMainActivity() {
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun initViewBinding(): ActivityAlarmBinding = ActivityAlarmBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmViewModel = viewModelOf(viewModelProvider)
}