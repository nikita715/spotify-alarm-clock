package ru.nikstep.alarm.util.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val DATABASE_NAME = "test1.db"
const val DATABASE_VERSION = 13

private val migrations = listOf(
    SimpleMigration(
        2, 3,
        arrayOf(
            "ALTER TABLE ALARM ADD COLUMN PREVIOUS_TRACK VARCHAR(50)"
        )
    ),
    SimpleMigration(
        3, 4,
        arrayOf(
            "CREATE TABLE `Playlist` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `external_id` TEXT NOT NULL)"
        )
    ),
    SimpleMigration(
        4, 5,
        emptyArray()
    ),
    SimpleMigration(
        5, 6,
        arrayOf(
            "DROP TABLE `Playlist`",
            "CREATE TABLE `Playlist` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `externalId` TEXT NOT NULL)"
        )
    ),
    SimpleMigration(
        6, 7,
        arrayOf(
            "DROP TABLE `Alarm`",
            "CREATE TABLE `Alarm` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'hour' INTEGER NOT NULL, 'minute' INTEGER NOT NULL, 'playlist' INTEGER NOT NULL, `previousTrack` TEXT, FOREIGN KEY('playlist') REFERENCES 'Playlist'('id'))"
        )
    ),
    SimpleMigration(
        7, 8,
        arrayOf(
            "DROP TABLE `Alarm`",
            "CREATE TABLE `Alarm` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'hour' INTEGER NOT NULL, 'minute' INTEGER NOT NULL, 'playlist' INTEGER NOT NULL REFERENCES 'Playlist'('id') ON DELETE CASCADE ON UPDATE, PREVIOUS_TRACK TEXT)"
        )
    ),
    SimpleMigration(
        8, 9,
        arrayOf(
            "CREATE TABLE `AlarmLog` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'year' INTEGER NOT NULL, 'month' INTEGER NOT NULL, 'day' INTEGER NOT NULL, 'hour' INTEGER NOT NULL, 'minute' INTEGER NOT NULL, 'second' INTEGER NOT NULL, 'playlist' TEXT NOT NULL)"
        )
    ),
    SimpleMigration(
        9, 10,
        arrayOf(
            "ALTER TABLE `AlarmLog` ADD COLUMN 'alarmId' INTEGER"
        )
    ),
    SimpleMigration(
        10, 11,
        arrayOf(
            "DROP TABLE `AlarmLog`",
            "CREATE TABLE `AlarmLog` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'year' INTEGER NOT NULL, 'month' INTEGER NOT NULL, 'day' INTEGER NOT NULL, 'hour' INTEGER NOT NULL, 'minute' INTEGER NOT NULL, 'second' INTEGER NOT NULL, 'playlist' TEXT NOT NULL, 'alarmId' INTEGER NOT NULL)"
        )
    ),
    SimpleMigration(
        11, 12,
        arrayOf(
            "ALTER TABLE `Alarm` ADD COLUMN 'active' INTEGER NOT NULL DEFAULT 1"
        )
    ),
    SimpleMigration(
        12, 13,
        arrayOf(
            "ALTER TABLE `Alarm` ADD COLUMN 'nextActive' INTEGER NOT NULL DEFAULT 1"
        )
    )
)

fun getMigrations(): Array<Migration> = migrations
    .map {
        object : Migration(it.fromVersion, it.toVersion) {
            override fun migrate(database: SupportSQLiteDatabase) {
                it.sqlCalls.forEach { sqlCall ->
                    database.execSQL(sqlCall)
                }
            }
        }
    }
    .toTypedArray()

private class SimpleMigration(
    val fromVersion: Int,
    val toVersion: Int,
    val sqlCalls: Array<String>
)