package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alarm: Alarm): Int

    @Query("DELETE FROM ALARM WHERE ID = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ALARM")
    suspend fun deleteAll()

    @Query("UPDATE ALARM SET nextActive = 0 where id = :alarmId")
    suspend fun disableNextAlarm(alarmId: Long)

    @Query("UPDATE ALARM SET nextActive = 1 where id = :alarmId")
    suspend fun enableNextAlarm(alarmId: Long)
}