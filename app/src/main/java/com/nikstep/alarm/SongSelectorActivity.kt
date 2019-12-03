package com.nikstep.alarm

import android.app.ListActivity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.net.toUri
import com.nikstep.alarm.service.MyAlarmManager
import java.io.File

class SongSelectorActivity : ListActivity() {

    private lateinit var alarmManager: MyAlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_selector)
        alarmManager = Dependencies.get(MyAlarmManager::class.java)!!
        val songs = alarmManager.getSongs() as Array<out File>
        val songNames = songs.map {
            val uri = Uri.parse(it.toUri().toString())
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(applicationContext, uri)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        }
        listAdapter = ArrayAdapter(applicationContext, R.layout.activity_song_selector, songNames)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val intent = Intent(applicationContext, AlarmActivity::class.java)
        getSharedPreferences(alarmPropertiesName, 0).edit().putInt(pAlarmSongIndex, id.toInt()).apply()
        Log.i("ss", id.toString())
        startActivity(intent)
    }

}