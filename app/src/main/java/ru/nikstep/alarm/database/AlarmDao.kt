package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.nikstep.alarm.model.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE id = :id")
    fun findById(id: Long): Alarm?

    @Insert
    fun insert(alarm: Alarm): Long

    @Query("DELETE FROM ALARM WHERE ID = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM ALARM")
    fun deleteAll()
}