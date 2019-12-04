package com.nikstep.alarm.android.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.nikstep.alarm.Dependencies
import com.nikstep.alarm.R
import com.nikstep.alarm.android.adapter.SongListAdapter
import com.nikstep.alarm.service.SongService

class SongListActivity : AppCompatActivity() {

    private val songService = Dependencies.get(SongService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_selector)
        initializeSongList()
    }

    private fun initializeSongList() {
        val listView: ListView = findViewById(R.id.song_list)
        val songNames = songService.findAll()
        val activeSong = songService.findOrCreateActiveSong()
        if (songNames.isNotEmpty() && activeSong != null) {
            listView.adapter =
                SongListAdapter(
                    applicationContext,
                    songNames,
                    activeSong
                )
        }
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { l: AdapterView<*>?, v: View?,
                                              position: Int, id: Long ->
                v?.apply {
                    songService.activateSong(id.toInt())
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }
    }
}