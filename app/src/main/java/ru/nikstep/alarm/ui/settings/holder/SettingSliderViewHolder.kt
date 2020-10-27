package ru.nikstep.alarm.ui.settings.holder

import ru.nikstep.alarm.databinding.SettingSliderItemBinding

class SettingSliderViewHolder(private val binding: SettingSliderItemBinding) : BondedViewHolder<Setting>(binding) {

    override fun bind(item: Setting) {
        item.itemBuilder.invoke(item, binding)
    }

}