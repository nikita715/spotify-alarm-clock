package ru.nikstep.alarm.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import ru.nikstep.alarm.api.model.PlaylistTracks
import ru.nikstep.alarm.api.model.Track

class PlaylistTracksJsonAdapter : JsonAdapter<PlaylistTracks>() {

    override fun fromJson(reader: JsonReader): PlaylistTracks? {
        reader.beginObject()
        val tracks: MutableList<Track> = mutableListOf()
        var trackId = 0L
        while (reader.hasNext()) {
            when (reader.selectName(JsonReader.Options.of("items"))) {
                0 -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        reader.beginObject()
                        when (reader.selectName(JsonReader.Options.of("track"))) {
                            0 -> {
                                reader.beginObject()
                                var trackName: String? = null
                                var trackSpotifyId: String? = null
                                var trackDurationMs: Long? = null
                                when (reader.selectName(JsonReader.Options.of("id", "name", "duration_ms"))) {
                                    0 -> trackName = reader.nextString()
                                    1 -> trackSpotifyId = reader.nextString()
                                    2 -> trackDurationMs = reader.nextLong()
                                    else -> {
                                        reader.skipName()
                                        reader.skipValue()
                                    }
                                }
                                if (trackName != null && trackSpotifyId != null && trackDurationMs != null) {
                                    tracks.add(
                                        Track(
                                            id = trackId++,
                                            name = trackName,
                                            duration = trackDurationMs.div(1000),
                                            externalId = trackSpotifyId
                                        )
                                    )
                                }
                                reader.endObject()
                            }
                            else -> {
                                reader.skipName()
                                reader.skipValue()
                            }
                        }
                        reader.endObject()
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
        return PlaylistTracks(tracks)
    }

    override fun toJson(writer: JsonWriter, value: PlaylistTracks?) {
        TODO("Not yet implemented")
    }
}