package ru.nikstep.alarm.client.spotify

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import ru.nikstep.alarm.BuildConfig
import javax.inject.Inject

class SpotifyClient @Inject constructor(
    private val context: Context
) {

    private val connectionParams = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
        .setRedirectUri(BuildConfig.SPOTIFY_REDIRECT_URI)
        .showAuthView(true)
        .build()

    fun play(id: String, type: SpotifyItemType) {
        when (type) {
            SpotifyItemType.PLAYLIST -> play("playlist:$id")
            SpotifyItemType.TRACK -> play("track:$id")
            SpotifyItemType.ALBUM -> play("album:$id")
            SpotifyItemType.ARTIST -> play("artist:$id")
        }
    }

    private fun play(item: String) {
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                appRemote.playerApi.play("spotify:$item", PlayerApi.StreamType.ALARM)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyClient", throwable.message, throwable)
            }
        })
    }

}