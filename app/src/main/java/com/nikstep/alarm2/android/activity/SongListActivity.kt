package com.nikstep.alarm2.android.activity

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.nikstep.alarm2.Dependencies
import com.nikstep.alarm2.R
import com.nikstep.alarm2.android.adapter.SongListAdapter
import com.nikstep.alarm2.service.SongService

class SongListActivity : AppCompatActivity() {

    private val songService = Dependencies.get(SongService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_selector)

        createList()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

    private fun createList() {
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
            AdapterView.OnItemClickListener { l: AdapterView<*>?, v: View?, position: Int, id: Long ->
                v?.apply {
                    val hyperspaceJumpAnimation =
                        AnimationUtils.loadAnimation(applicationContext, R.anim.animation)
                    startAnimation(hyperspaceJumpAnimation)
                    songService.activate(id.toInt())
                    createList()
                }
            }
    }
}