package ru.nikstep.alarm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.util.database.DATABASE_VERSION

@Database(entities = [Alarm::class, Playlist::class, AlarmLog::class], version = DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun alarmLogDao(): AlarmLogDao
}