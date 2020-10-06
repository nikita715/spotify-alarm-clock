package ru.nikstep.alarm.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.nikstep.alarm.model.Playlists
import ru.nikstep.alarm.model.SpotifyUser

interface SpotifyApiClient {

    @GET("/v1/me")
    suspend fun getMe(@Header("Authorization") authHeader: String?): SpotifyUser

    @GET("/v1/users/{username}/playlists")
    suspend fun getPlaylists(
        @Path("username") username: String?,
        @Header("Authorization") authHeader: String?
    ): Playlists

}