package com.nikstep.alarm.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.nikstep.alarm.database.SongDatabase
import com.nikstep.alarm.model.Song
import com.nikstep.alarm.musicFilesPath
import java.io.File

class SongService(
    private val context: Context,
    private val songDatabase: SongDatabase
) {

    fun updateSongs() {
        getMusicFiles().forEachIndexed { index, it ->
            val uri = Uri.parse(it.toUri().toString())
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, uri)
            val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
            val singer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            songDatabase.save(Song(-1, index == 0, title, singer, duration, it.name))
        }
    }

    private fun getMusicFiles(): Array<File> =
        context.getExternalFilesDir(musicFilesPath)?.listFiles() ?: emptyArray()

    fun findOrCreateActiveSong(): Song? {
        val activeSong = songDatabase.findActive() ?: songDatabase.findById(0)
        return if (activeSong != null) activeSong else {
            updateSongs()
            val firstSong = songDatabase.findById(0)
            if (firstSong != null) {
                activateSong(firstSong.id)
                firstSong
            } else null
        }
    }

    fun changeActiveSong(activeSong: Song) {
        songDatabase.deactivate()
        if (songDatabase.exists(activeSong.id + 1)) {
            activateSong(activeSong.id + 1)
        } else if (songDatabase.exists(0)) {
            activateSong(0)
        }
    }

    fun activateSong(id: Int) {
        songDatabase.deactivate()
        songDatabase.activate(id)
    }

    fun findAll(): List<Song> = songDatabase.findAll()

}

