package ru.nikstep.alarm.ui.alarmlog

import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.databinding.AlarmLogItemBinding
import ru.nikstep.alarm.model.AlarmLog

class AlarmLogListViewHolder(private val binding: AlarmLogItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(alarmLog: AlarmLog, onItemClickListener: (AlarmLog) -> Unit) {
        binding.root.tag = alarmLog.id
        binding.alarmLogText.text = alarmLog.run {
            "$year-${month.toString().padStart(2, '0')}-${
                day.toString().padStart(2, '0')
            } ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${
                second.toString().padStart(2, '0')
            } - ${alarmLog.playlist}"
        }
        binding.root.setOnClickListener {
            onItemClickListener(alarmLog)
        }
    }

}