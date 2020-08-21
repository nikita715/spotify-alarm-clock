package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val playlist: String? = null,
    @ColumnInfo(name = "PREVIOUS_TRACK") val previousTrack: String? = null
) : Identifiable