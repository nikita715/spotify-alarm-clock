package com.nikstep.alarm.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.nikstep.alarm.database.helper.DbHelper
import com.nikstep.alarm.database.model.AlarmTableDef
import com.nikstep.alarm.database.model.parseAlarmFrom
import com.nikstep.alarm.database.model.toContentValues
import com.nikstep.alarm.model.Alarm


class AlarmDatabase(context: Context) {
    private val dbHelper = DbHelper(context)

    fun findById(id: Int): Alarm? {
        val cursor = dbHelper.readableDatabase.query(
            AlarmTableDef.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf("1"), null, null, null
        )
        return getOne(cursor)
    }

    fun save(alarm: Alarm) {
        val writableDatabase = dbHelper.writableDatabase
        val id = writableDatabase.insertWithOnConflict(
            AlarmTableDef.TABLE_NAME,
            null,
            alarm.toContentValues(),
            SQLiteDatabase.CONFLICT_IGNORE
        )
        if (id == -1L) {
            writableDatabase.update(
                AlarmTableDef.TABLE_NAME,
                alarm.toContentValues(),
                "${BaseColumns._ID} = ?",
                arrayOf("1")
            )
        }
    }

    private fun getOne(cursor: Cursor): Alarm? {
        return if (cursor.moveToNext()) {
            val alarm = parseAlarmFrom(cursor)
            cursor.close()
            alarm
        } else null
    }

    fun delete() {
        dbHelper.writableDatabase.delete(
            AlarmTableDef.TABLE_NAME,
            null, null
        )
    }

}