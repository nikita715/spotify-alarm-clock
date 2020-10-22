package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
class AlarmLog(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo val alarmId: Long,
    @ColumnInfo val year: Int,
    @ColumnInfo val month: Int,
    @ColumnInfo val day: Int,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val second: Int,
    @ColumnInfo val playlist: String
) : Identifiable {

    constructor(alarmId: Long, playlist: String, calendarInstance: Calendar = Calendar.getInstance()) : this(
        alarmId = alarmId,
        year = calendarInstance.get(Calendar.YEAR),
        month = calendarInstance.get(Calendar.MONTH) + 1,
        day = calendarInstance.get(Calendar.DAY_OF_MONTH),
        hour = calendarInstance.get(Calendar.HOUR_OF_DAY),
        minute = calendarInstance.get(Calendar.MINUTE),
        second = calendarInstance.get(Calendar.SECOND),
        playlist = playlist
    )

    val yearAsString: String
        get() = year.toString()
    val monthAsString: String
        get() = month.toTwoDigitString()
    val dayAsString: String
        get() = day.toTwoDigitString()
    val hourAsString: String
        get() = hour.toTwoDigitString()
    val minuteAsString: String
        get() = minute.toTwoDigitString()
    val secondAsString: String
        get() = second.toTwoDigitString()

    private fun Int.toTwoDigitString() = toString().padStart(2, '0')
}