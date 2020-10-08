package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.database.PlaylistDao
import ru.nikstep.alarm.model.Playlist

class DatabasePlaylistDataService(
    private val playlistDao: PlaylistDao
) : PlaylistDataService {

    override fun findById(playlistId: Long): Playlist? = playlistDao.findById(playlistId)

    override fun findAll(): List<Playlist> = playlistDao.getAll()

    override fun saveAll(playlists: List<Playlist>): List<Long> = playlistDao.insertAll(playlists)

    override fun deleteAll() = playlistDao.deleteAll()
}