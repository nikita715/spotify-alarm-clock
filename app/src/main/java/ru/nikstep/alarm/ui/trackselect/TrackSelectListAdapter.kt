package ru.nikstep.alarm.ui.trackselect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikstep.alarm.api.model.Track
import ru.nikstep.alarm.databinding.TrackItemBinding
import ru.nikstep.alarm.util.diffItemCallback

class TrackSelectListAdapter(
    data: List<Track> = mutableListOf(),
    private val onItemClickListener: (i: Track) -> Unit = {}
) : ListAdapter<Track, TrackSelectViewHolder>(diffItemCallback()) {

    private val data = data as MutableList<Track>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSelectViewHolder =
        TrackSelectViewHolder(
            TrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = data.size

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(data: List<Track>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAllItems() {
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TrackSelectViewHolder, position: Int) =
        holder.bind(data[position], onItemClickListener)
}
