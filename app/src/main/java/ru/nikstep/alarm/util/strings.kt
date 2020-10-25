package ru.nikstep.alarm.util

fun Int.toTwoDigitString() = toString().padStart(2, '0')