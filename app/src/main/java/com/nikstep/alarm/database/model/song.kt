package com.nikstep.alarm.database.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm.model.Song

object SongTableDef : BaseColumns {
    const val TABLE_NAME = "song"
    const val COLUMN_NAME_ACTIVE = "active"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SINGER = "singer"
    const val COLUMN_NAME_DURATION = "duration"
    const val COLUMN_NAME_FILE_NAME = "file_name"
}

const val SQL_CREATE_SONG_TABLE =
    "CREATE TABLE ${SongTableDef.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${SongTableDef.COLUMN_NAME_ACTIVE} INTEGER," +
            "${SongTableDef.COLUMN_NAME_TITLE} TEXT," +
            "${SongTableDef.COLUMN_NAME_SINGER} TEXT," +
            "${SongTableDef.COLUMN_NAME_DURATION} INTEGER," +
            "${SongTableDef.COLUMN_NAME_FILE_NAME} TEXT)"

const val SQL_DROP_SONG_TABLE = "DROP TABLE IF EXISTS ${SongTableDef.TABLE_NAME}"

fun parseSongFrom(cursor: Cursor) = Song(
    cursor.getInt(0),
    cursor.getInt(1) == 1,
    cursor.getString(2),
    cursor.getString(3),
    cursor.getInt(4),
    cursor.getString(5)
)

fun Song.toContentValues() = ContentValues().apply {
    put(SongTableDef.COLUMN_NAME_ACTIVE, if (active) 1 else 0)
    put(SongTableDef.COLUMN_NAME_TITLE, title)
    put(SongTableDef.COLUMN_NAME_SINGER, singer)
    put(SongTableDef.COLUMN_NAME_DURATION, duration)
    put(SongTableDef.COLUMN_NAME_FILE_NAME, fileName)
}