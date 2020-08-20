package ru.nikstep.alarm.model

data class Alarm(
    override val id: Long = -1L,
    val hour: Int,
    val minute: Int,
    val playlist: String? = null
) : Identifiable