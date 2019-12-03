package com.nikstep.alarm2.android.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SongArrayAdapter(
    context: Context, resource: Int, objects: List<String>, private val activeSong: String
) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if ((view as TextView).text == activeSong) {
            view.setBackgroundColor(Color.GREEN)
        } else {
            view.setBackgroundColor(Color.WHITE)
        }
        return view
    }


}