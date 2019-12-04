package com.nikstep.alarm.model

import java.time.DayOfWeek

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<DayOfWeek>
)