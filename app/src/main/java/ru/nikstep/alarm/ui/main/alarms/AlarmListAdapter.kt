package ru.nikstep.alarm.ui.main.alarms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.nikstep.alarm.databinding.AlarmItemBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.util.diffItemCallback

class AlarmListAdapter(
    data: List<Alarm>,
    private val onItemClickListener: (i: Alarm) -> Unit = {},
    private val onSwitchClickListener: (i: Alarm, switch: SwitchMaterial) -> Unit = { i, switch -> }
) : ListAdapter<Alarm, AlarmListViewHolder>(diffItemCallback()) {

    private var data: MutableList<Alarm> = data as MutableList<Alarm>

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
        this.data = data as MutableList<Alarm>
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = data[position].id

    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) =
        holder.bind(data[position], onItemClickListener, onSwitchClickListener)
}
