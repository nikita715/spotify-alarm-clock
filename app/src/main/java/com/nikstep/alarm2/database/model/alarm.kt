package com.nikstep.alarm2.database.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm2.model.Alarm
import java.time.DayOfWeek

object AlarmTableDef : BaseColumns {
    const val TABLE_NAME = "alarm"
    const val COLUMN_NAME_INDEX = "index"
    const val COLUMN_NAME_TIME = "time"
    const val COLUMN_NAME_DAYS_OF_WEEK = "days-of-week"
}

const val SQL_CREATE_ALARM_TABLE =
    "CREATE TABLE ${AlarmTableDef.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${AlarmTableDef.COLUMN_NAME_INDEX} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_TIME} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_DAYS_OF_WEEK} TEXT)"

const val SQL_DROP_ALARM_TABLE = "DROP TABLE IF EXISTS ${AlarmTableDef.TABLE_NAME}"

fun parseAlarmFrom(cursor: Cursor) = Alarm(
    cursor.getInt(1),
    cursor.getLong(2),
    cursor.getString(3).split(",").map { DayOfWeek.valueOf(it) }.toSet()
)

fun Alarm.toContentValues() = ContentValues().apply {
    put(BaseColumns._ID, index)
    put(AlarmTableDef.COLUMN_NAME_INDEX, index)
    put(AlarmTableDef.COLUMN_NAME_TIME, time)
    put(AlarmTableDef.COLUMN_NAME_DAYS_OF_WEEK, daysOfWeek.joinToString { it.name })
}