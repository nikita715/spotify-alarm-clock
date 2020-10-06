package ru.nikstep.alarm.util.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import ru.nikstep.alarm.model.SpotifyUser

class SpotifyUserJsonAdapter : JsonAdapter<SpotifyUser>() {
    private val propertyNames = JsonReader.Options.of("id")

    override fun fromJson(reader: JsonReader): SpotifyUser? {
        reader.beginObject()
        var name: String? = null
        while (reader.hasNext()) {
            when (reader.selectName(propertyNames)) {
                0 -> name = reader.nextString()
                else -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return name?.let { SpotifyUser(-1, it) }
    }

    override fun toJson(writer: JsonWriter, value: SpotifyUser?) {
        TODO("Not yet implemented")
    }
}