package ru.nikstep.alarm.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nikstep.alarm.ui.alarm.AlarmViewModel
import ru.nikstep.alarm.ui.alarmlog.AlarmLogViewModel
import ru.nikstep.alarm.ui.main.MainViewModel
import ru.nikstep.alarm.ui.notifications.NotificationsViewModel
import ru.nikstep.alarm.ui.playlists.PlaylistsViewModel
import ru.nikstep.alarm.ui.trackselect.TrackSelectViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlarmViewModel::class)
    abstract fun bindAlarmViewModel(viewModel: AlarmViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistsViewModel::class)
    abstract fun playlistsViewModel(viewModel: PlaylistsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun notificationsViewModel(viewModel: NotificationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlarmLogViewModel::class)
    abstract fun alarmLogViewModel(viewModel: AlarmLogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrackSelectViewModel::class)
    abstract fun trackSelectViewModel(viewModel: TrackSelectViewModel): ViewModel
}