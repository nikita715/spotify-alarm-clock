package ru.nikstep.alarm.util.date

import java.util.Calendar
import java.util.GregorianCalendar

fun formatDate(hour: Int, minute: Int) =
    hour.toString().padStart(2, '0') + ":" + minute.toString().padStart(2, '0')

class CurrentDateInfo private constructor(
    private val calendar: Calendar = Calendar.getInstance()
) {
    val year: Int
        get() = calendar.get(Calendar.YEAR)
    val month: Int
        get() = calendar.get(Calendar.MONTH)
    val day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)
    val hour: Int
        get() = calendar.get(Calendar.HOUR)
    val minute: Int
        get() = calendar.get(Calendar.MINUTE)
    val second: Int
        get() = calendar.get(Calendar.SECOND)

    operator fun plus(type: Int) = CurrentDateInfoChange(this, type, CalendarChangeType.PLUS)

    operator fun plusAssign(data: CurrentDateInfoChangeData): Unit = calendar.add(data.type, data.value)

    operator fun minus(type: Int) = CurrentDateInfoChange(this, type, CalendarChangeType.MINUS)

    class CurrentDateInfoChange internal constructor(
        private val currentDateInfo: CurrentDateInfo,
        private val type: Int,
        private val calendarChangeType: CalendarChangeType
    ) {
        infix fun of(value: Int) = currentDateInfo.also {
            when (calendarChangeType) {
                CalendarChangeType.PLUS -> it.calendar.add(type, value)
                CalendarChangeType.MINUS -> it.calendar.roll(type, value)
            }
        }
    }

    class CurrentDateInfoChangeData constructor(
        internal val value: Int,
        internal val type: Int
    )

    internal enum class CalendarChangeType {
        PLUS,
        MINUS
    }

    companion object {
        fun getCurrentDate() = CurrentDateInfo()
    }
}

infix fun Int.of(type: Int): CurrentDateInfo.CurrentDateInfoChangeData =
    CurrentDateInfo.CurrentDateInfoChangeData(this, type)

fun buildNextAlarmCalendar(hour: Int, minute: Int): Calendar {
    val year: Int
    val month: Int
    val dayOfMonth: Int

    CurrentDateInfo.getCurrentDate().also { dateInfo ->
        if (dateInfo.hour >= hour && dateInfo.minute >= minute) {
            dateInfo += 1 of Calendar.DAY_OF_MONTH
        }
        dayOfMonth = dateInfo.day
        year = dateInfo.year
        month = dateInfo.month
    }

    return GregorianCalendar(year, month, dayOfMonth, hour, minute)
}

fun buildNextAlarmReminderCalendar(hour: Int, minute: Int, minutesBeforeTheAlarm: Int): Calendar =
    buildNextAlarmCalendar(hour, minute).also { it.add(Calendar.MINUTE, -minutesBeforeTheAlarm) }