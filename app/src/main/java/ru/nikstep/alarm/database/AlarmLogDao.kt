package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nikstep.alarm.model.AlarmLog

@Dao
interface AlarmLogDao {
    @Query("SELECT * FROM AlarmLog")
    suspend fun getAll(): List<AlarmLog>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarmLog: AlarmLog): Long

    @Query("DELETE FROM AlarmLog")
    suspend fun deleteAll()
}