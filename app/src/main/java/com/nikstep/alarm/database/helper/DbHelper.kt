package com.nikstep.alarm.database.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nikstep.alarm.database.model.SQL_CREATE_ALARM_TABLE
import com.nikstep.alarm.database.model.SQL_CREATE_SONG_TABLE
import com.nikstep.alarm.database.model.SQL_DROP_ALARM_TABLE
import com.nikstep.alarm.database.model.SQL_DROP_SONG_TABLE

class DbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_SONG_TABLE)
        db.execSQL(SQL_CREATE_ALARM_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_SONG_TABLE)
        db.execSQL(SQL_DROP_ALARM_TABLE)
        db.execSQL(SQL_CREATE_SONG_TABLE)
        db.execSQL(SQL_CREATE_ALARM_TABLE)
    }

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "AppAlarm.db"
    }
}