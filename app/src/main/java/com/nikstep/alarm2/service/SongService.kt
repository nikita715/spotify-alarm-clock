package com.nikstep.alarm2.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm2.database.SongDatabase
import com.nikstep.alarm2.model.Song
import java.io.File

class SongService(
    private val context: Context
) {
    private val songDatabase = SongDatabase(context)

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
                activate(firstSong.id)
                firstSong
            } else null
        }
    }

    fun changeActiveSong(activeSong: Song) {
        deactivate(activeSong.id)
        if (exists(activeSong.id + 1)) {
            activate(activeSong.id)
        } else if (exists(0)) {
            activate(0)
        }
    }

    fun findById(id: Int): Song? = songDatabase.findById(id)

    fun deactivate(id: Int) = songDatabase.deactivate(id)

    fun exists(id: Int): Boolean = songDatabase.exists(id)

    fun activate(id: Int) = songDatabase.activate(id)

    fun findAll(): List<Song> = songDatabase.findAll()
}

