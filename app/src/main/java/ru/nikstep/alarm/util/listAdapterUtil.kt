package ru.nikstep.alarm.util

import androidx.recyclerview.widget.DiffUtil
import ru.nikstep.alarm.model.Identifiable

fun <T : Identifiable> diffItemCallback() = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.equals(newItem)

}
