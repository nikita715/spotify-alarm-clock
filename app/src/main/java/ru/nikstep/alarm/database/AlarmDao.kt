package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.nikstep.alarm.model.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE id = :id")
    fun findById(id: Long): Alarm?

    @Insert
    fun insert(alarm: Alarm): Long

    @Update
    fun update(alarm: Alarm): Int

    @Query("UPDATE ALARM SET HOUR = :hour AND MINUTE = :minute AND PLAYLIST = :playlist WHERE ID = :alarmId")
    fun updateSettings(alarmId: Long, hour: Int, minute: Int, playlist: String): Int

    @Query("DELETE FROM ALARM WHERE ID = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM ALARM")
    fun deleteAll()
}