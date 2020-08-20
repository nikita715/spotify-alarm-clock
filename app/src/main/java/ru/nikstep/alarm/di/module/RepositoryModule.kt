package ru.nikstep.alarm.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.database.AppDatabase
import ru.nikstep.alarm.service.AlarmManager
import ru.nikstep.alarm.service.AlarmService

@Module
object RepositoryModule {

    @Provides
    @Reusable
    fun roomDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "AlarmApplication.db")
            .allowMainThreadQueries().build()

    @Provides
    @Reusable
    fun alarmDao(appDatabase: AppDatabase) =
        appDatabase.alarmDao()

    @Provides
    @Reusable
    fun alarmService(alarmDao: AlarmDao) =
        AlarmService(alarmDao)

    @Provides
    @Reusable
    fun alarmManager(application: Application, alarmService: AlarmService) =
        AlarmManager(application, alarmService)
}