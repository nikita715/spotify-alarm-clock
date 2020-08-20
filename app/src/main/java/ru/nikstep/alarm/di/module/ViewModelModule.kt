package ru.nikstep.alarm.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nikstep.alarm.ui.main.ActivityMainViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActivityMainViewModel::class)
    abstract fun bindActivityMainViewModel(viewModel: ActivityMainViewModel): ViewModel
}