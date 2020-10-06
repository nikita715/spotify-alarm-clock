package ru.nikstep.alarm.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.util.data.Resource
import javax.inject.Inject

class PlaylistsViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : ViewModel() {
    fun getPlaylists() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = spotifyApiService.getPlaylists()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }
}