package ru.nikstep.alarm.service.data

import ru.nikstep.alarm.model.Playlist

/**
 * Manages stored playlists
 */
interface PlaylistDataService {

    /**
     * Find a playlist by [playlistId]
     */
    fun findById(playlistId: Long): Playlist?

    /**
     * Find all playlists
     */
    fun findAll(): List<Playlist>

    /**
     * Create [playlists]
     */
    fun saveAll(playlists: List<Playlist>): List<Playlist>

    /**
     * Delete all playlists
     */
    fun deleteAll()
}