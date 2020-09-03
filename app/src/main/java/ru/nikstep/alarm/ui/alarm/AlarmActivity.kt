package ru.nikstep.alarm.ui.alarm

import android.os.Bundle
import android.view.View
import ru.nikstep.alarm.R
import ru.nikstep.alarm.data.AlarmData
import ru.nikstep.alarm.databinding.ActivityAlarmBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.ui.main.MainActivity
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

        val alarm = intent.extras?.getLong("alarmId")
            ?.let(viewModel::getAlarm)
            ?.also { alarm ->
                binding.timePicker.hour = alarm.hour
                binding.timePicker.minute = alarm.minute
                binding.playlistNameInput.setText(alarm.playlist)
            }

        if (alarm == null) binding.button2.visibility = View.GONE

        binding.timePicker.setIs24HourView(true)
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
        intent.extras?.getLong("alarmId")?.let { viewModel.removeAlarm(it) }
        returnToMainActivity()
        overridePendingTransition(R.anim.open_main_activity, R.anim.close_alarm_activity)
    }

    private fun returnToMainActivity() = startActivityWithIntent(applicationContext, MainActivity::class.java)

    override fun initViewBinding(): ActivityAlarmBinding = ActivityAlarmBinding.inflate(layoutInflater)

    override fun initViewModel(): AlarmViewModel = viewModelOf(viewModelProvider)
}