package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.database.PlaylistDao
import ru.nikstep.alarm.model.Playlist

class DatabasePlaylistDataService(
    private val playlistDao: PlaylistDao
) : PlaylistDataService {

    override fun findById(playlistId: Long): Playlist? = playlistDao.findById(playlistId)

    override fun findAll(): List<Playlist> = playlistDao.getAll()

    override fun saveAll(playlists: List<Playlist>): List<Playlist> {
        playlistDao.insert(playlists)
        return findAll()
    }

    override fun createOrUpdate(playlists: List<Playlist>): List<Playlist> {
        val storedPlaylists = findAll().associateBy { it.externalId }
        return saveAll(
            playlists.map {
                val playlist = storedPlaylists[it.externalId] ?: return@map it
                it.copy(id = playlist.id)
            }
        )

    }

    override fun deleteAll() = playlistDao.deleteAll()
}