package com.nikstep.alarm2.database.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nikstep.alarm2.database.model.SQL_CREATE_ALARM_TABLE

class AlarmDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ALARM_TABLE)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Alarm.db"
    }
}