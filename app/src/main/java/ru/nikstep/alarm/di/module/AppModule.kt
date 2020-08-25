package ru.nikstep.alarm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nikstep.alarm.service.AlarmReceiver
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.main.MainActivity

@Module
abstract class AppModule {
    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [RepositoryModule::class])
    abstract fun alarmActivity(): AlarmActivity

    @ContributesAndroidInjector
    abstract fun alarmReceiver(): AlarmReceiver
}