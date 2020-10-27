package ru.nikstep.alarm.ui.settings.holder

import ru.nikstep.alarm.databinding.SettingItemBinding
import ru.nikstep.alarm.ui.common.BondedViewHolder

class SettingsViewHolder(private val binding: SettingItemBinding) : BondedViewHolder<Setting>(binding) {

    override fun bind(item: Setting) {
        item.itemBuilder.invoke(item, binding)
    }

}