package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.model.Playlist

/**
 * Manages stored playlists
 */
interface PlaylistDataService {

    /**
     * Find a playlist by [playlistId]
     */
    suspend fun findById(playlistId: Long): Playlist?

    /**
     * Find all playlists
     */
    suspend fun findAll(): List<Playlist>

    /**
     * Create [playlists]
     */
    suspend fun saveAll(playlists: List<Playlist>): List<Playlist>

    /**
     * Delete all playlists
     */
    suspend fun deleteAll()

    /**
     * Create or update [playlists]
     */
    suspend fun createOrUpdate(playlists: List<Playlist>): List<Playlist>
}