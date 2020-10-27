package ru.nikstep.alarm.ui.settings.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BondedViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)

}