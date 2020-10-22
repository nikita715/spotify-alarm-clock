package ru.nikstep.alarm.util.json

import com.squareup.moshi.Moshi
import ru.nikstep.alarm.api.model.PlaylistTracks
import ru.nikstep.alarm.api.model.Playlists
import ru.nikstep.alarm.api.model.SpotifyPlaylistImage
import ru.nikstep.alarm.api.model.SpotifyUser

private val jsonAdapters = listOf(
    Playlists::class.java to PlaylistJsonAdapter(),
    SpotifyUser::class.java to SpotifyUserJsonAdapter(),
    SpotifyPlaylistImage::class.java to SpotifyPlaylistImageJsonAdapter(),
    PlaylistTracks::class.java to PlaylistTracksJsonAdapter()
)

fun Moshi.Builder.addJsonAdapters(): Moshi.Builder =
    jsonAdapters.forEach { add(it.first, it.second) }.let { this }