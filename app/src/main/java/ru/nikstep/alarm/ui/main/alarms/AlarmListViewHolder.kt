package ru.nikstep.alarm.ui.main.alarms

import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.databinding.AlarmItemBinding
import ru.nikstep.alarm.model.Alarm

class AlarmListViewHolder(private val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(alarm: Alarm, onItemClickListener: (i: Alarm) -> Unit) {
        binding.alarmText.text = "${alarm.hour}:${alarm.minute}"
        binding.root.setOnClickListener {
            onItemClickListener(alarm)
        }
    }

}