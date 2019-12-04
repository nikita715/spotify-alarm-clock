package com.nikstep.alarm.model

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<Int>
)