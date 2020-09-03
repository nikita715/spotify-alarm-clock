package ru.nikstep.alarm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nikstep.alarm.service.alarm.AlarmReceiver
import ru.nikstep.alarm.service.alarm.StopAlarmService
import ru.nikstep.alarm.ui.alarm.AlarmActivity
import ru.nikstep.alarm.ui.main.MainActivity
import ru.nikstep.alarm.ui.notifications.NotificationsActivity
import ru.nikstep.alarm.ui.playlists.PlaylistsActivity

@Module
abstract class AppModule {
    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun alarmActivity(): AlarmActivity

    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun playlistsActivity(): PlaylistsActivity

    @ContributesAndroidInjector(modules = [DependencyModule::class])
    abstract fun notificationsActivity(): NotificationsActivity

    @ContributesAndroidInjector
    abstract fun alarmReceiver(): AlarmReceiver

    @ContributesAndroidInjector
    abstract fun stopAlarmService(): StopAlarmService
}