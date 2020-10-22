package ru.nikstep.alarm.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url
import ru.nikstep.alarm.api.model.PlaylistTracks
import ru.nikstep.alarm.api.model.Playlists
import ru.nikstep.alarm.api.model.SpotifyPlaylistImage
import ru.nikstep.alarm.api.model.SpotifyUser

interface SpotifyApiClient {

    @GET("/v1/me")
    suspend fun getMe(@Header("Authorization") authHeader: String?): SpotifyUser

    @GET("/v1/users/{username}/playlists")
    suspend fun getPlaylists(
        @Path("username") username: String?,
        @Header("Authorization") authHeader: String?
    ): Playlists

    @GET("/v1/playlists/{playlistId}/images")
    suspend fun getPlaylistCoverLink(
        @Path("playlistId") playlistId: String,
        @Header("Authorization") authHeader: String?
    ): SpotifyPlaylistImage?

    @GET("/v1/playlists/{playlistId}/tracks")
    suspend fun getPlaylistTracks(
        @Path("playlistId") playlistId: String,
        @Header("Authorization") authHeader: String?
    ): PlaylistTracks

    @GET
    @Streaming
    suspend fun getPlaylistCover(@Url imageUrl: String): ResponseBody

}