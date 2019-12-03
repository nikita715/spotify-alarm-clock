package com.nikstep.alarm2.android.activity

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ListView
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.android.adapter.SongListAdapter
import com.nikstep.alarm2.service.SongService


class SongListActivity : ListActivity() {

    private val songService = Dependencies.get(SongService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_selector)
        createSongAdapter()
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        v?.apply {
            val hyperspaceJumpAnimation =
                AnimationUtils.loadAnimation(applicationContext, R.anim.animation)
            startAnimation(hyperspaceJumpAnimation)
            songService.activate(id.toInt())
            createSongAdapter()
        }
    }

    private fun createSongAdapter() {
        val songNames = songService.findAll()
        val activeSong = songService.findOrCreateActiveSong()
        if (songNames.isNotEmpty() && activeSong != null) {
            listAdapter =
                SongListAdapter(
                    applicationContext,
                    songNames,
                    activeSong
                )
        }
    }
}