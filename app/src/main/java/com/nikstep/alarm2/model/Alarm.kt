package com.nikstep.alarm2.model

import java.time.DayOfWeek

class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<DayOfWeek>
)