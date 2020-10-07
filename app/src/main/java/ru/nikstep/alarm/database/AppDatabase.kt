package ru.nikstep.alarm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nikstep.alarm.model.Alarm

@Database(entities = [Alarm::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun playlistDao(): PlaylistDao
}