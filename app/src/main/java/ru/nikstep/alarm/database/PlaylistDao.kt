package ru.nikstep.alarm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.nikstep.alarm.model.Playlist

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    fun findById(playlistId: Long): Playlist?

    @Query("SELECT * FROM playlist")
    fun getAll(): List<Playlist>

    @Query("DELETE FROM playlist")
    fun deleteAll()

    @Insert
    fun insertAll(playlists: List<Playlist>): List<Playlist>
}