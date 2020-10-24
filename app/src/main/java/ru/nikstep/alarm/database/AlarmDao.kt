package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.nikstep.alarm.model.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    suspend fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE id = :id")
    suspend fun findById(id: Long): Alarm?

    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Update
    suspend fun update(alarm: Alarm): Int

    @Query("UPDATE ALARM SET HOUR = :hour AND MINUTE = :minute AND PLAYLIST = :playlist WHERE ID = :alarmId")
    suspend fun updateSettings(alarmId: Long, hour: Int, minute: Int, playlist: Long): Int

    @Query("DELETE FROM ALARM WHERE ID = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ALARM")
    suspend fun deleteAll()
}