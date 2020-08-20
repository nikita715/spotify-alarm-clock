package ru.nikstep.alarm.database.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import ru.nikstep.alarm.model.Alarm

object AlarmTableDef : BaseColumns {
    const val TABLE_NAME = "alarm"
    const val COLUMN_NAME_HOUR = "hour"
    const val COLUMN_NAME_MINUTE = "minute"
    const val COLUMN_NAME_PLAYLIST = "playlist"
}

const val SQL_CREATE_ALARM_TABLE =
    "CREATE TABLE ${AlarmTableDef.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${AlarmTableDef.COLUMN_NAME_HOUR} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_MINUTE} INTEGER," +
            "${AlarmTableDef.COLUMN_NAME_PLAYLIST} VARCHAR(50)"

const val SQL_DROP_ALARM_TABLE = "DROP TABLE IF EXISTS ${AlarmTableDef.TABLE_NAME}"

fun parseAlarmFrom(cursor: Cursor) = Alarm(
    cursor.getLong(0),
    cursor.getInt(1),
    cursor.getInt(2),
    cursor.getString(3)
)

fun Alarm.toContentValues() = ContentValues().apply {
    put(BaseColumns._ID, 1L)
    put(AlarmTableDef.COLUMN_NAME_HOUR, hour)
    put(AlarmTableDef.COLUMN_NAME_MINUTE, minute)
    put(AlarmTableDef.COLUMN_NAME_PLAYLIST, playlist)
}