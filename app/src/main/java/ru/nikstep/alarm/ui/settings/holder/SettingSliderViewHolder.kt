package ru.nikstep.alarm.ui.settings.holder

import ru.nikstep.alarm.databinding.SettingSliderItemBinding
import ru.nikstep.alarm.ui.common.BondedViewHolder

class SettingSliderViewHolder(private val binding: SettingSliderItemBinding) : BondedViewHolder<Setting>(binding) {

    override fun bind(item: Setting) {
        item.itemBuilder.invoke(item, binding)
    }

}