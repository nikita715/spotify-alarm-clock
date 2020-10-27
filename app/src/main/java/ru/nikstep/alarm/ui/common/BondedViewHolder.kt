package ru.nikstep.alarm.ui.common

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.nikstep.alarm.model.Identifiable

abstract class BondedViewHolder<T : Identifiable>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)

}