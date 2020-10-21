package ru.nikstep.alarm.ui.alarmlog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikstep.alarm.databinding.AlarmLogItemBinding
import ru.nikstep.alarm.model.AlarmLog
import ru.nikstep.alarm.util.diffItemCallback

class AlarmLogListAdapter(
    data: List<AlarmLog>,
    private val onItemClickListener: (i: AlarmLog) -> Unit = {}
) : ListAdapter<AlarmLog, AlarmLogListViewHolder>(diffItemCallback()) {

    private val data = data as MutableList<AlarmLog>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmLogListViewHolder =
        AlarmLogListViewHolder(
            AlarmLogItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = data.size

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(data: List<AlarmLog>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = data[position].id

    override fun onBindViewHolder(holder: AlarmLogListViewHolder, position: Int) =
        holder.bind(data[position], onItemClickListener)
}
