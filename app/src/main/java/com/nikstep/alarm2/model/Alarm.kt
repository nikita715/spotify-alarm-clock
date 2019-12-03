package com.nikstep.alarm2.model

import java.time.DayOfWeek

class Alarm(
    val index: Int,
    val time: Long,
    val daysOfWeek: Set<DayOfWeek>
)