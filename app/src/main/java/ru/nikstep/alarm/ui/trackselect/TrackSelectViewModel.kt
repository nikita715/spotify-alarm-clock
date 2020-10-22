package ru.nikstep.alarm.ui.trackselect

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.api.model.Track
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.util.data.Resource
import javax.inject.Inject

class TrackSelectViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : ViewModel() {

    fun getPlaylistTracks(playlistId: String): LiveData<Resource<List<Track>>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))
            try {
                emit(Resource.success(data = spotifyApiService.getPlaylistTracks(playlistId)))
            } catch (e: Exception) {
                Log.e("PlaylistsViewModel", e.message, e)
                emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

}