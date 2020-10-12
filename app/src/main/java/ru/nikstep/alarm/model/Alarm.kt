package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Playlist::class,
        parentColumns = ["id"],
        childColumns = ["playlist"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val playlist: Long,
    @ColumnInfo(name = "PREVIOUS_TRACK") val previousTrack: String? = null
) : Identifiable