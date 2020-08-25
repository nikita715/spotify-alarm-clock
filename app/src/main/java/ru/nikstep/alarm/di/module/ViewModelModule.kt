package ru.nikstep.alarm.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nikstep.alarm.ui.alarm.AlarmViewModel
import ru.nikstep.alarm.ui.main.MainViewModel

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
}