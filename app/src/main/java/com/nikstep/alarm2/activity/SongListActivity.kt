package com.nikstep.alarm2.activity

import android.app.ListActivity
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import com.nikstep.alarm.R
import com.nikstep.alarm.musicFilesPath
import com.nikstep.alarm2.database.SongDatabase
import com.nikstep.alarm2.model.Song
import java.io.File

class SongListActivity : ListActivity() {

    private val songDatabase by lazy {
        SongDatabase(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        listAdapter = ArrayAdapter(applicationContext, 0, songDatabase.findAll())
    }

    fun updateSongs(view: View) {
        getMusicFiles().forEachIndexed { index, it ->
            val uri = Uri.parse(it.toUri().toString())
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(applicationContext, uri)
            val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()
            val singer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            songDatabase.save(Song(-1, index == 0, title, singer, duration, it.name))
        }
    }

    private fun getMusicFiles(): Array<File> =
        applicationContext.getExternalFilesDir(musicFilesPath)?.listFiles() ?: emptyArray()

}