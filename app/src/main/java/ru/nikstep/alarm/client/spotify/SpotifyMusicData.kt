package ru.nikstep.alarm.client.spotify

import com.spotify.protocol.types.PlayerState

class SpotifyMusicData(
    id: String,
    val type: SpotifyItemType,
    val playType: SpotifyPlayType,
    val previousTrack: String? = null,
    val enabled: Boolean = true,
    val callback: (suspend (PlayerState) -> Unit)? = null
) {
    val uri = "spotify:${type.typeName}:$id"
}