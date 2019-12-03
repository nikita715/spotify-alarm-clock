package com.nikstep.alarm2.android.activity

import android.app.ListActivity
import android.os.Bundle
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.android.adapter.SongArrayAdapter
import com.nikstep.alarm2.service.SongService

class SongListActivity : ListActivity() {

    private val songService = Dependencies.get(SongService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_selector)

        listAdapter = SongArrayAdapter(
            applicationContext,
            R.layout.list_item,
            songService.findAllSongNames(),
            2
        )
    }

}