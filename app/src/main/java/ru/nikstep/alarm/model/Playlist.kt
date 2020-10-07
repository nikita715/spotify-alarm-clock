package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val externalId: String
) : Identifiable