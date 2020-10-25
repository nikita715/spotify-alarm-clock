package ru.nikstep.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.nikstep.alarm.util.toTwoDigitString

@Entity(
    foreignKeys = [ForeignKey(
        entity = Playlist::class,
        parentColumns = ["id"],
        childColumns = ["playlist"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val playlist: Long,
    @ColumnInfo val active: Boolean = true,
    @ColumnInfo(name = "PREVIOUS_TRACK") val previousTrack: String? = null
) : Identifiable {

    fun getTimeAsString(): String = "${hour.toTwoDigitString()}:${minute.toTwoDigitString()}"

}