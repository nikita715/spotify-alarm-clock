package com.nikstep.alarm2.database.model

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm2.model.Song

object SongTableDef : BaseColumns {
    const val TABLE_NAME = "song"
    const val COLUMN_NAME_INDEX = "index"
    const val COLUMN_NAME_ACTIVE = "active"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_SINGER = "singer"
    const val COLUMN_NAME_DURATION = "duration"
    const val COLUMN_NAME_FILE_NAME = "file-name"
}

const val SQL_CREATE_SONG_TABLE =
    "CREATE TABLE ${SongTableDef.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${SongTableDef.COLUMN_NAME_INDEX} INTEGER," +
            "${SongTableDef.COLUMN_NAME_ACTIVE} INTEGER," +
            "${SongTableDef.COLUMN_NAME_TITLE} TEXT," +
            "${SongTableDef.COLUMN_NAME_SINGER} TEXT," +
            "${SongTableDef.COLUMN_NAME_DURATION} INTEGER," +
            "${SongTableDef.COLUMN_NAME_FILE_NAME} TEXT)"

const val SQL_DROP_SONG_TABLE = "DROP TABLE IF EXISTS ${SongTableDef.TABLE_NAME}"

fun parseSongFrom(cursor: Cursor) = Song(
    cursor.getInt(1),
    cursor.getInt(2) == 1,
    cursor.getString(3),
    cursor.getString(4),
    cursor.getInt(5),
    cursor.getString(6)
)

fun Song.toContentValues() = ContentValues().apply {
    put(BaseColumns._ID, index)
    put(SongTableDef.COLUMN_NAME_INDEX, index)
    put(SongTableDef.COLUMN_NAME_ACTIVE, if (active) 1 else 0)
    put(SongTableDef.COLUMN_NAME_TITLE, title)
    put(SongTableDef.COLUMN_NAME_SINGER, singer)
    put(SongTableDef.COLUMN_NAME_DURATION, duration)
    put(SongTableDef.COLUMN_NAME_FILE_NAME, fileName)
}