package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nikstep.alarm.util.date.CurrentDateInfo
import ru.nikstep.alarm.util.toTwoDigitString
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

    constructor(
        alarmId: Long,
        playlist: String,
        currentDate: CurrentDateInfo = CurrentDateInfo.getCurrentDate() + 1 of Calendar.MINUTE
    ) : this(
        alarmId = alarmId,
        year = currentDate.year,
        month = currentDate.month,
        day = currentDate.day,
        hour = currentDate.hour,
        minute = currentDate.minute,
        second = currentDate.second,
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
}