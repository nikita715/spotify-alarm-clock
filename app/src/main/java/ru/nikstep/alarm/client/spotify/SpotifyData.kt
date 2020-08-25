package ru.nikstep.alarm.client.spotify

import com.spotify.protocol.types.PlayerState

class SpotifyData(
    id: String,
    val type: SpotifyItemType,
    val playType: SpotifyPlayType,
    val previousTrack: String? = null,
    val callback: ((PlayerState) -> Unit)? = null
) {
    val uri = "spotify:${type.typeName}:$id"
}