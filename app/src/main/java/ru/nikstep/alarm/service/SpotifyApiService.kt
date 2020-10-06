package ru.nikstep.alarm.service

import ru.nikstep.alarm.api.SpotifyApiClient
import ru.nikstep.alarm.model.Playlist
import javax.inject.Inject

class SpotifyApiService @Inject constructor(
    private val spotifyApiClient: SpotifyApiClient,
    private val loginService: LoginService
) {

    private var me: String? = null
    private var authHeader: String? = null

    suspend fun getPlaylists(): List<Playlist> {
        if (me == null) {
            me = getMe().name
        }
        return spotifyApiClient.getPlaylists(me, loginService.getAccessToken()).playlistList
    }

    private suspend fun getMe() = spotifyApiClient.getMe(loginService.getAccessToken())

}