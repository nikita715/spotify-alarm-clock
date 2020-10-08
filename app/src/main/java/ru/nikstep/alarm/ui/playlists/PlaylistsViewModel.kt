package ru.nikstep.alarm.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.service.data.PlaylistDataService
import ru.nikstep.alarm.util.data.Resource
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService,
    private val playlistDataService: PlaylistDataService
) : ViewModel() {
    fun downloadPlaylists(): LiveData<Resource<List<Playlist>>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = spotifyApiService.getPlaylists()))
            } catch (e: Exception) {
                emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun savePlaylists(playlists: List<Playlist>): List<Playlist> = playlistDataService.saveAll(playlists)

    fun getPlaylists() = playlistDataService.findAll()
}