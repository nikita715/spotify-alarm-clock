package ru.nikstep.alarm.ui.notifications

import android.os.Bundle
import ru.nikstep.alarm.R
import ru.nikstep.alarm.databinding.ActivityNotificationsBinding
import ru.nikstep.alarm.ui.base.BaseActivity
import ru.nikstep.alarm.util.viewmodel.viewModelOf

class NotificationsActivity : BaseActivity<NotificationsViewModel, ActivityNotificationsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.menu.findItem(R.id.notificationsPage).isVisible = false
    }

    override fun initViewBinding(): ActivityNotificationsBinding = ActivityNotificationsBinding.inflate(layoutInflater)

    override fun initViewModel(): NotificationsViewModel = viewModelOf(viewModelProvider)
}