package ru.nikstep.alarm.ui.trackselect

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nikstep.alarm.api.model.Track
import ru.nikstep.alarm.service.SpotifyApiService
import ru.nikstep.alarm.util.data.Result
import ru.nikstep.alarm.util.data.emitLiveData
import javax.inject.Inject

class TrackSelectViewModel @Inject constructor(
    private val spotifyApiService: SpotifyApiService
) : ViewModel() {

    fun getPlaylistTracks(playlistId: String): LiveData<Result<List<Track>>> = emitLiveData {
        spotifyApiService.getPlaylistTracks(playlistId)
    }

}