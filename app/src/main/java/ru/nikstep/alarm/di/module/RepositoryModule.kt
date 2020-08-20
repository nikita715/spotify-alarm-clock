package ru.nikstep.alarm.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.nikstep.alarm.database.AlarmRepository
import ru.nikstep.alarm.database.helper.DbHelper
import ru.nikstep.alarm.service.AlarmManager
import ru.nikstep.alarm.service.AlarmService

@Module
object RepositoryModule {

    @Provides
    @Reusable
    fun dbHelper(context: Context) =
        DbHelper(context)

    @Provides
    @Reusable
    fun alarmRepository(dbHelper: DbHelper) =
        AlarmRepository(dbHelper)

    @Provides
    @Reusable
    fun alarmService(alarmRepository: AlarmRepository) =
        AlarmService(alarmRepository)

    @Provides
    @Reusable
    fun alarmManager(context: Context, alarmService: AlarmService) =
        AlarmManager(context, alarmService)
}