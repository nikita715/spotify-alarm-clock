package ru.nikstep.alarm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.Playlist

@Database(entities = [Alarm::class, Playlist::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun playlistDao(): PlaylistDao
}