package ru.nikstep.alarm.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import ru.nikstep.alarm.model.SpotifyPlaylistImage

class SpotifyPlaylistImageJsonAdapter : JsonAdapter<SpotifyPlaylistImage>() {
    private val propertyNames = JsonReader.Options.of("url")

    override fun fromJson(reader: JsonReader): SpotifyPlaylistImage? {
        var url: String? = null

        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.selectName(propertyNames)) {
                    0 -> url = reader.nextString()
                    else -> {
                        reader.skipName()
                        reader.skipValue()
                    }
                }
            }
            if (url != null && (url.contains("mosaic.scdn.co/60/"))) {
                reader.endObject()
                reader.endArray()
                return SpotifyPlaylistImage(url)
            }
            reader.endObject()
        }
        reader.endArray()
        return url?.let { SpotifyPlaylistImage(it) } ?: throw RuntimeException("Didn't get an image link")
    }

    override fun toJson(writer: JsonWriter, value: SpotifyPlaylistImage?) {
        TODO("Not yet implemented")
    }
}