package ru.nikstep.alarm.ui.trackselect

import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.api.model.Track
import ru.nikstep.alarm.databinding.TrackItemBinding

class TrackSelectViewHolder(private val binding: TrackItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track, onItemClickListener: (Track) -> Unit) {
//        binding.root.tag = alarmLog.id
        binding.songText.text = ""
        binding.root.setOnClickListener {
            onItemClickListener(track)
        }
    }

}