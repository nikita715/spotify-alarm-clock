package ru.nikstep.alarm.ui.main.alarms

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.nikstep.alarm.databinding.AlarmItemBinding
import ru.nikstep.alarm.model.Alarm
import ru.nikstep.alarm.util.date.formatDate

class AlarmListViewHolder(private val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        alarm: Alarm, onItemClickListener: (i: Alarm) -> Unit,
        onSwitchClickListener: (i: Alarm, switch: SwitchMaterial) -> Unit
    ) {
        binding.root.tag = alarm.id
        binding.alarmText.text = formatDate(alarm.hour, alarm.minute)
        binding.root.setOnClickListener {
            onItemClickListener(alarm)
        }
        binding.alarmSwitch.isChecked = alarm.active
        binding.alarmSwitch.apply {
            setOnClickListener {
                onSwitchClickListener(alarm, binding.alarmSwitch)
            }
            isChecked = true
            isEnabled = true
        }
    }

}