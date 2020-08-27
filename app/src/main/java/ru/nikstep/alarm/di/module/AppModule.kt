package ru.nikstep.alarm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nikstep.alarm.service.alarm.AlarmReceiver
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.main.MainActivity

@Module
abstract class AppModule {
    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun alarmActivity(): AlarmActivity

    @ContributesAndroidInjector
    abstract fun alarmReceiver(): AlarmReceiver
}