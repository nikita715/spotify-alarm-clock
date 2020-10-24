package ru.nikstep.alarm.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikstep.alarm.databinding.PlaylistItemBinding
import ru.nikstep.alarm.model.Playlist
import ru.nikstep.alarm.util.diffItemCallback

class PlaylistListAdapter(
    data: List<Playlist> = mutableListOf(),
    private val onItemClickListener: (i: Playlist) -> Unit = {}
) : ListAdapter<Playlist, PlaylistListViewHolder>(diffItemCallback()) {

    private val data = data as MutableList<Playlist>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder =
        PlaylistListViewHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = data.size

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(data: List<Playlist>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = data[position].id

    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) =
        holder.bind(data[position], onItemClickListener)
}
