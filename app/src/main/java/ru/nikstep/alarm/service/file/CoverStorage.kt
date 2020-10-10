package ru.nikstep.alarm.service.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.FileNotFoundException

class CoverStorage(
    private val context: Context
) {

    fun savePlaylistCover(spotifyId: String, cover: Bitmap) =
        cover.compress(Bitmap.CompressFormat.JPEG, 100, context.openFileOutput(spotifyId, Context.MODE_PRIVATE))

    fun readPlaylistCover(spotifyId: String): Bitmap? = try {
        BitmapFactory.decodeStream(context.openFileInput(spotifyId))
    } catch (e: FileNotFoundException) {
        Log.e("CoverStorage", "File not found: $spotifyId")
        null
    }

}