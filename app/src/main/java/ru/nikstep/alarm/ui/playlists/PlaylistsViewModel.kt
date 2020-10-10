package ru.nikstep.alarm.ui.playlists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.service.file.CoverStorage
import ru.nikstep.alarm.util.data.Resource
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService,
    private val playlistDataService: PlaylistDataService,
    private val coverStorage: CoverStorage
) : ViewModel() {
    fun downloadPlaylists(): LiveData<Resource<List<Playlist>>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                val data = spotifyApiService.getPlaylists()
                data.forEach { playlist ->
                    spotifyApiService.getPlaylistCover(playlist.externalId)?.let { playlistCover ->
                        coverStorage.savePlaylistCover(playlist.externalId, playlistCover)
                    }
                }
                emit(Resource.success(data = data))
            } catch (e: Exception) {
                Log.e("PlaylistsViewModel", e.message, e)
                emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun savePlaylists(playlists: List<Playlist>): List<Playlist> = playlistDataService.createOrUpdate(playlists)
        .let(this::populateWithCovers)

    fun getPlaylists() = playlistDataService.findAll().let(this::populateWithCovers)

    private fun populateWithCovers(playlists: List<Playlist>): List<Playlist> =
        playlists.onEach { playlist ->
            playlist.coverImage = coverStorage.readPlaylistCover(playlist.externalId)
        }
}