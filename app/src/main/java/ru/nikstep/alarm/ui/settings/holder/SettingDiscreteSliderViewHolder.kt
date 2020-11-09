package ru.nikstep.alarm.ui.settings.holder

import ru.nikstep.alarm.databinding.SettingDiscreteSliderItemBinding
import ru.nikstep.alarm.ui.common.BondedViewHolder

class SettingDiscreteSliderViewHolder(private val binding: SettingDiscreteSliderItemBinding) :
    BondedViewHolder<Setting>(binding) {

    override fun bind(item: Setting) {
        item.itemBuilder.invoke(item, binding)
    }

}