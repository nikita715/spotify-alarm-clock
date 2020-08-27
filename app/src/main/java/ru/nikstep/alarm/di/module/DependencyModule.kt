package ru.nikstep.alarm.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import ru.nikstep.alarm.client.spotify.SpotifyClient
import ru.nikstep.alarm.database.AlarmDao
import ru.nikstep.alarm.database.AppDatabase
import ru.nikstep.alarm.service.alarm.AndroidAlarmController
import ru.nikstep.alarm.service.alarm.AndroidAlarmManager
import ru.nikstep.alarm.service.alarm.DatabaseAlarmDataService
import ru.nikstep.alarm.service.log.LogService
import ru.nikstep.alarm.service.log.ToastLogService

@Module
object DependencyModule {

    @Provides
    @Reusable
    fun roomDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "AlarmApplication.db")
            .addMigrations(object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE ALARM ADD COLUMN PREVIOUS_TRACK VARCHAR(50)")
                }
            })
            .allowMainThreadQueries().build()

    @Provides
    @Reusable
    fun alarmDao(appDatabase: AppDatabase) =
        appDatabase.alarmDao()

    @Provides
    @Reusable
    fun alarmService(alarmDao: AlarmDao) =
        DatabaseAlarmDataService(alarmDao)

    @Provides
    @Reusable
    fun spotifyClient(application: Application) =
        SpotifyClient(application)

    @Provides
    @Reusable
    fun alarmManager(application: Application) =
        AndroidAlarmManager(application)

    @Provides
    @Reusable
    fun logService(application: Application): LogService =
        ToastLogService(application)

    @Provides
    @Reusable
    fun alarmController(
        alarmManager: AndroidAlarmManager,
        alarmDataService: DatabaseAlarmDataService,
        spotifyClient: SpotifyClient,
        logService: LogService
    ) =
        AndroidAlarmController(alarmManager, alarmDataService, spotifyClient, logService)
}