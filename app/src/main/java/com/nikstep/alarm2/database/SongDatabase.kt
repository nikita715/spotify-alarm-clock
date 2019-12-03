package com.nikstep.alarm2.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.nikstep.alarm2.database.helper.SongDbHelper
import com.nikstep.alarm2.database.model.AlarmTableDef
import com.nikstep.alarm2.database.model.SongTableDef
import com.nikstep.alarm2.database.model.parseSongFrom
import com.nikstep.alarm2.database.model.toContentValues
import com.nikstep.alarm2.model.Song

class SongDatabase(context: Context) {
    private val songDbHelper = SongDbHelper(context)

    fun findById(id: Int): Song? {
        val cursor = songDbHelper.readableDatabase.query(
            SongTableDef.TABLE_NAME,
            null,
            BaseColumns._ID,
            arrayOf(id.toString()), null, null, null
        )
        return getOne(cursor)
    }

    fun findAll(): List<Song> {
        val cursor = songDbHelper.readableDatabase.query(
            SongTableDef.TABLE_NAME,
            null, null, null, null, null, null
        )
        return getAll(cursor)
    }

    fun findActive(): Song? {
        val cursor = songDbHelper.readableDatabase.query(
            SongTableDef.TABLE_NAME,
            null,
            SongTableDef.COLUMN_NAME_ACTIVE,
            arrayOf("1"), null, null, null
        )
        return getOne(cursor)
    }

    fun exists(id: Int): Boolean {
        val cursor = songDbHelper.readableDatabase.query(
            SongTableDef.TABLE_NAME,
            null,
            BaseColumns._ID,
            arrayOf(id.toString()), null, null, null
        )
        val hasNext = cursor.moveToNext()
        cursor.close()
        return hasNext
    }

    fun activate(id: Int) {
        val contentValues = ContentValues()
        contentValues.put(SongTableDef.COLUMN_NAME_ACTIVE, 1)
        songDbHelper.writableDatabase.update(
            SongTableDef.TABLE_NAME,
            contentValues,
            BaseColumns._ID,
            arrayOf(id.toString())
        )
    }

    fun deactivate(id: Int) {
        val contentValues = ContentValues()
        contentValues.put(SongTableDef.COLUMN_NAME_ACTIVE, 0)
        songDbHelper.writableDatabase.update(
            SongTableDef.TABLE_NAME,
            contentValues,
            BaseColumns._ID,
            arrayOf(id.toString())
        )
    }

    fun save(song: Song) {
        songDbHelper.writableDatabase.insert(AlarmTableDef.TABLE_NAME, null, song.toContentValues())
    }

    private fun getOne(cursor: Cursor): Song? {
        return if (cursor.moveToNext()) {
            val song = parseSongFrom(cursor)
            cursor.close()
            song
        } else null
    }

    private fun getAll(cursor: Cursor): List<Song> {
        val songs = mutableListOf<Song>()
        while (cursor.moveToNext()) {
            songs.add(parseSongFrom(cursor))
        }
        cursor.close()
        return songs
    }

}