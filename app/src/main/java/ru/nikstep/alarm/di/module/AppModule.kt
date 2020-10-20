package ru.nikstep.alarm.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.nikstep.alarm.service.alarm.android.AlarmReceiver
import ru.nikstep.alarm.service.alarm.android.AlarmService
import ru.nikstep.alarm.service.alarm.android.RescheduleAlarmsService
import ru.nikstep.alarm.service.alarm.android.StopAlarmService
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
    abstract fun alarmBroadcastReceiver(): AlarmReceiver

    @ContributesAndroidInjector
    abstract fun alarmService(): AlarmService

    @ContributesAndroidInjector
    abstract fun rescheduleAlarmsService(): RescheduleAlarmsService

    @ContributesAndroidInjector
    abstract fun stopAlarmService(): StopAlarmService
}