package ru.nikstep.alarm.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val externalId: String
) : Identifiable {
    override fun equals(other: Any?): Boolean =
        other is Playlist && name == other.name && externalId == other.externalId

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + externalId.hashCode()
        return result
    }

    @Ignore
    var coverImage: Bitmap? = null
}