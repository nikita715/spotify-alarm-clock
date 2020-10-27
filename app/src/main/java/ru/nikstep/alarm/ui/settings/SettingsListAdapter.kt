package ru.nikstep.alarm.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nikstep.alarm.databinding.SettingItemBinding
import ru.nikstep.alarm.databinding.SettingSliderItemBinding
import ru.nikstep.alarm.ui.settings.holder.BondedViewHolder
import ru.nikstep.alarm.ui.settings.holder.Setting
import ru.nikstep.alarm.ui.settings.holder.SettingSliderViewHolder
import ru.nikstep.alarm.ui.settings.holder.SettingType
import ru.nikstep.alarm.ui.settings.holder.SettingsViewHolder
import ru.nikstep.alarm.util.diffItemCallback

class SettingsListAdapter(
    data: List<Setting> = mutableListOf()
) : ListAdapter<Setting, BondedViewHolder<Setting>>(diffItemCallback()) {

    private val data = data as MutableList<Setting>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BondedViewHolder<Setting> =
        when (viewType) {
            SettingType.SLIDER.number ->
                SettingSliderViewHolder(SettingSliderItemBinding.inflate(LayoutInflater.from(parent.context)))
            else -> SettingsViewHolder(SettingItemBinding.inflate(LayoutInflater.from(parent.context)))
        }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = getItem(position).type.number

    override fun getItem(position: Int): Setting = data[position]

    override fun getItemId(position: Int): Long = data[position].id

    override fun onBindViewHolder(holder: BondedViewHolder<Setting>, position: Int) =
        holder.bind(data[position])
}
