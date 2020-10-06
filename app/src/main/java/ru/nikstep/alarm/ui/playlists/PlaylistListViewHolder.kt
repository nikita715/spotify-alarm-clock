package ru.nikstep.alarm.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.databinding.PlaylistItemBinding
import ru.nikstep.alarm.model.Playlist

class PlaylistListViewHolder(private val binding: PlaylistItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist, onItemClickListener: (Playlist) -> Unit) {
        binding.root.tag = playlist.id
        binding.alarmText.text = playlist.name
        binding.root.setOnClickListener {
            onItemClickListener(playlist)
        }
    }


}