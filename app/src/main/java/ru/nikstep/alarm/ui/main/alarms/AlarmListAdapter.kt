package ru.nikstep.alarm.ui.main.alarms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikstep.alarm.databinding.AlarmItemBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.util.diffItemCallback

class AlarmListAdapter(
    private val data: MutableList<Alarm>,
    private val onItemClickListener: (i: Alarm) -> Unit = {}
) : ListAdapter<Alarm, AlarmListViewHolder>(diffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListViewHolder =
        AlarmListViewHolder(
            AlarmItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = data.size

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(data: List<Alarm>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = data[position].id

    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) =
        holder.bind(data[position], onItemClickListener)
}
