package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nikstep.alarm.model.Playlist

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun findById(playlistId: Long): Playlist?

    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<Playlist>

    @Query("DELETE FROM playlist")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlists: List<Playlist>): List<Long>
}