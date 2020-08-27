package ru.nikstep.alarm.client.spotify

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.PlayerApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.internal.PlayerApiImpl
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.RemoteClient
import com.spotify.protocol.types.Empty
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.UriWithOptionExtras
import ru.nikstep.alarm.BuildConfig
import ru.nikstep.alarm.client.MusicClient
import javax.inject.Inject
import kotlin.random.Random

class SpotifyClient @Inject constructor(
    private val context: Context
) : MusicClient<SpotifyMusicData> {

    private val connectionParams = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
        .setRedirectUri(BuildConfig.SPOTIFY_REDIRECT_URI)
        .showAuthView(true)
        .build()

    private val internalClientField = PlayerApiImpl::class.java.getDeclaredField("mClient")
        .apply { isAccessible = true }

    override fun play(data: SpotifyMusicData) {
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                appRemote.playerApi.subscribeToPlayerContext().setEventCallback {
                    Log.i("SpotifyClient", it.toString())
                }
                appRemote.playerApi.subscribeToPlayerState().setEventCallback {
                    Log.i("SpotifyClient", it.toString())
                    data.callback?.invoke(it)
                }
                appRemote.connectApi.connectSwitchToLocalDevice()
                when (data.type) {
                    SpotifyItemType.TRACK -> playTrack(data, appRemote)
                    SpotifyItemType.PLAYLIST, SpotifyItemType.ALBUM ->
                        playSetOfTracks(data, appRemote)
                }
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyClient", throwable.message, throwable)
            }
        })
    }

    private fun playTrack(data: SpotifyMusicData, appRemote: SpotifyAppRemote) =
        appRemote.playerApi.play(data.uri, PlayerApi.StreamType.ALARM)

    private fun playSetOfTracks(data: SpotifyMusicData, appRemote: SpotifyAppRemote) =
        appRemote.contentApi.getChildrenOfItem(
            ListItem(data.uri, null, null, null, null, true, true), Int.MAX_VALUE, 0
        ).setResultCallback { listItems ->
            val nextIndex: Int = when (data.playType) {
                SpotifyPlayType.NEXT ->
                    listItems.items.indexOfFirst { it.uri == data.previousTrack }.plus(1).let {
                        if (it >= listItems.total || it == -1) 0 else it
                    }
                SpotifyPlayType.RANDOM -> Random(System.currentTimeMillis()).nextInt(listItems.total)
            }
            appRemote.playerApi.playAlarmTrackInPlaylist(data.uri, nextIndex)
        }

    private fun PlayerApi.playAlarmTrackInPlaylist(uri: String, i: Int): CallResult<Empty> {
        val mClient = internalClientField.get(this) as RemoteClient
        return mClient.call(
            "com.spotify.play_spotify_uri_option_extras",
            UriWithOptionExtras(uri, arrayOf(i.toString(), "alarm")),
            Empty::class.java
        )
    }

}