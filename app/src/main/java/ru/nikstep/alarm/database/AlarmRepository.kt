package ru.nikstep.alarm.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import ru.nikstep.alarm.database.helper.DbHelper
import ru.nikstep.alarm.database.model.AlarmTableDef
import ru.nikstep.alarm.database.model.parseAlarmFrom
import ru.nikstep.alarm.database.model.toContentValues
import ru.nikstep.alarm.model.Alarm

class AlarmRepository(
    private val dbHelper: DbHelper
) {

    fun findById(alarmId: Long): Alarm? {
        val cursor = dbHelper.readableDatabase.query(
            AlarmTableDef.TABLE_NAME,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(alarmId.toString()), null, null, null
        )
        return getOne(cursor)
    }

    fun save(alarm: Alarm): Alarm {
        val writableDatabase = dbHelper.writableDatabase
        val id = writableDatabase.insertWithOnConflict(
            AlarmTableDef.TABLE_NAME,
            null,
            alarm.toContentValues(),
            SQLiteDatabase.CONFLICT_REPLACE
        )
        return Alarm(id, alarm.hour, alarm.minute, alarm.playlist)
    }

    private fun getOne(cursor: Cursor): Alarm? {
        return if (cursor.moveToNext()) {
            val alarm = parseAlarmFrom(cursor)
            cursor.close()
            alarm
        } else null
    }

    fun delete(alarmId: Long) {
        dbHelper.writableDatabase.delete(
            AlarmTableDef.TABLE_NAME,
            "${BaseColumns._ID} = ?", arrayOf(alarmId.toString())
        )
    }

    fun findAll(): List<Alarm> {
        val cursor = dbHelper.readableDatabase.query(
            AlarmTableDef.TABLE_NAME, null, null,
            null, null, null, BaseColumns._ID
        )
        return getAll(cursor)
    }

    private fun getAll(cursor: Cursor): List<Alarm> {
        val alarms = mutableListOf<Alarm>()
        while (cursor.moveToNext()) {
            alarms.add(parseAlarmFrom(cursor))
        }
        cursor.close()
        return alarms
    }

}