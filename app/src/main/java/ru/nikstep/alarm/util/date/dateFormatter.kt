package ru.nikstep.alarm.util.date

fun formatDate(hour: Int, minute: Int) =
    hour.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')