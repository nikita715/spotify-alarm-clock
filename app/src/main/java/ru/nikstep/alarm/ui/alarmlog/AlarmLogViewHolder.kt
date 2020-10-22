package ru.nikstep.alarm.ui.alarmlog

import androidx.recyclerview.widget.RecyclerView
import ru.nikstep.alarm.databinding.AlarmLogItemBinding
import ru.nikstep.alarm.model.AlarmLog

class AlarmLogViewHolder(private val binding: AlarmLogItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(alarmLog: AlarmLog, onItemClickListener: (AlarmLog) -> Unit) {
        binding.root.tag = alarmLog.id
        binding.alarmLogText.text = alarmLog.run {
            "$yearAsString-$monthAsString-$dayAsString " +
                    "$hourAsString:$minuteAsString:$secondAsString - $playlist"
        }
        binding.root.setOnClickListener {
            onItemClickListener(alarmLog)
        }
    }

}