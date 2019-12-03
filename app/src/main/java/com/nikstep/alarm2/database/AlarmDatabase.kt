package com.nikstep.alarm2.database

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm2.database.helper.DbHelper
import com.nikstep.alarm2.database.model.AlarmTableDef
import com.nikstep.alarm2.database.model.parseAlarmFrom
import com.nikstep.alarm2.database.model.toContentValues
import com.nikstep.alarm2.model.Alarm

class AlarmDatabase(context: Context) {
    private val dbHelper = DbHelper(context)

    fun findById(id: Int): Alarm? {
        val cursor = dbHelper.readableDatabase.query(
            AlarmTableDef.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString()), null, null, null
        )
        return getOne(cursor)
    }

    fun save(alarm: Alarm) {
        dbHelper.writableDatabase.insert(
            AlarmTableDef.TABLE_NAME,
            null,
            alarm.toContentValues()
        )
    }

    private fun getOne(cursor: Cursor): Alarm? {
        return if (cursor.moveToNext()) {
            val alarm = parseAlarmFrom(cursor)
            cursor.close()
            alarm
        } else null
    }

}