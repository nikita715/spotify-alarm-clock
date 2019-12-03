package com.nikstep.alarm2.database.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm2.model.Alarm
import java.time.DayOfWeek

object AlarmTableDef : BaseColumns {
    const val TABLE_NAME = "alarm"
    const val COLUMN_NAME_INDEX = "index"
    const val COLUMN_NAME_HOUR = "hour"
    const val COLUMN_NAME_MINUTE = "minute"
    const val COLUMN_NAME_DAYS_OF_WEEK = "days-of-week"
}

const val SQL_CREATE_ALARM_TABLE =
    "CREATE TABLE ${AlarmTableDef.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${AlarmTableDef.COLUMN_NAME_INDEX} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_HOUR} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_MINUTE} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_DAYS_OF_WEEK} TEXT)"

const val SQL_DROP_ALARM_TABLE = "DROP TABLE IF EXISTS ${AlarmTableDef.TABLE_NAME}"

fun parseAlarmFrom(cursor: Cursor) = Alarm(
    cursor.getInt(1),
    cursor.getInt(2),
    cursor.getInt(3),
    cursor.getString(4).split(",").map { DayOfWeek.valueOf(it) }.toSet()
)

fun Alarm.toContentValues() = ContentValues().apply {
    put(BaseColumns._ID, index)
    put(AlarmTableDef.COLUMN_NAME_INDEX, index)
    put(AlarmTableDef.COLUMN_NAME_HOUR, hour)
    put(AlarmTableDef.COLUMN_NAME_MINUTE, minute)
    put(AlarmTableDef.COLUMN_NAME_DAYS_OF_WEEK, daysOfWeek.joinToString { it.name })
}