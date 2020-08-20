package ru.nikstep.alarm.ui.main

import android.os.Bundle
import android.view.View
import ru.nikstep.alarm.databinding.ActivityMainBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class MainActivity : BaseActivity<ActivityMainViewModel, ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun setAlarm(view: View) {
        viewModel.setAlarm(
            hour = binding.timePicker.hour,
            minute = binding.timePicker.minute,
            playlist = binding.playlistNameInput.text.toString()
        )
    }

    fun removeAlarm(view: View) {
        viewModel.removeAlarm(1L)
    }

    override fun initViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initViewModel(): ActivityMainViewModel = viewModelOf(viewModelProvider)
}
