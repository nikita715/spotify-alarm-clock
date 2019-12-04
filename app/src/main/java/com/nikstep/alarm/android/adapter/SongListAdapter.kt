package com.nikstep.alarm.android.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.nikstep.alarm.R
import com.nikstep.alarm.model.Song

class SongListAdapter(
    private val context: Context,
    private val data: List<Song>,
    private val activeSong: Song
) : BaseAdapter() {

    override fun getCount() = data.size

    private val inflater by lazy {
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItem(position: Int): Any {
        return data[position].buildFullName()
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vi: View? = convertView ?: inflater.inflate(R.layout.list_item, null)
        val textView = vi?.findViewById(R.id.song_record) as TextView
        val currentSong = data[position]
        textView.text = currentSong.buildFullName()
        if (currentSong == activeSong) {
            vi.setBackgroundColor(Color.GREEN)
        } else {
            vi.setBackgroundColor(Color.WHITE)
        }
        return vi
    }
}
