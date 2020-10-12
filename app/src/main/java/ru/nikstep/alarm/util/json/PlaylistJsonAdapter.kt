package ru.nikstep.alarm.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import ru.nikstep.alarm.api.model.Playlists
import ru.nikstep.alarm.model.Playlist


class PlaylistJsonAdapter : JsonAdapter<Playlists>() {

    override fun fromJson(reader: JsonReader): Playlists? {
        reader.beginObject()
        val playlists: MutableList<Playlist> = mutableListOf()
        while (reader.hasNext()) {
            when (reader.selectName(JsonReader.Options.of("items"))) {
                0 -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        var playlistId: String? = null
                        var playlistName: String? = null
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.selectName(JsonReader.Options.of("id", "name"))) {
                                0 -> playlistId = reader.nextString()
                                1 -> playlistName = reader.nextString()
                                else -> {
                                    reader.skipName()
                                    reader.skipValue()
                                }
                            }
                        }
                        reader.endObject()
                        if (playlistId != null && playlistName != null) {
                            playlists.add(Playlist(name = playlistName, externalId = playlistId))
                        }
                    }
                    reader.endArray()
                }
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return Playlists(playlists)
    }

    override fun toJson(writer: JsonWriter, value: Playlists?) {
        TODO("Not yet implemented")
    }
}