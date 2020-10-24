package ru.nikstep.alarm.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.file.CoverStorage
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService,
    private val playlistDataService: PlaylistDataService,
    private val coverStorage: CoverStorage
) : ViewModel() {

    fun downloadPlaylists(): LiveData<Result<List<Playlist>>> = emitLiveData {
        spotifyApiService.getPlaylists().also { playlists ->
            playlists.forEach { playlist ->
                spotifyApiService.getPlaylistCover(playlist.externalId)?.let { playlistCover ->
                    coverStorage.savePlaylistCover(playlist.externalId, playlistCover)
                }
            }
        }
    }

    fun savePlaylists(playlists: List<Playlist>): LiveData<Result<List<Playlist>>> = emitLiveData {
        playlistDataService.createOrUpdate(playlists).let(this::populateWithCovers)
    }

    fun getPlaylists(): LiveData<Result<List<Playlist>>> = emitLiveData {
        playlistDataService.findAll().let(this::populateWithCovers)
    }

    private fun populateWithCovers(playlists: List<Playlist>): List<Playlist> =
        playlists.onEach { playlist ->
            playlist.coverImage = coverStorage.readPlaylistCover(playlist.externalId)
        }
}