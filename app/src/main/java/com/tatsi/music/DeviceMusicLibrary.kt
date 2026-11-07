package com.tatsi.music

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.tatsi.music.model.Track
import java.util.Locale
import java.util.concurrent.TimeUnit

object DeviceMusicLibrary {
    fun scan(context: Context, likedIds: Set<String>): List<Track> {
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val result = mutableListOf<Track>()
        context.contentResolver.query(collection, projection, selection, null, "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC")?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val uri = ContentUris.withAppendedId(collection, id)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(cursor.getLong(durationCol).coerceAtLeast(0L)).toInt()
                result += Track(
                    id = uri.toString(),
                    title = cursor.getString(titleCol)?.takeIf { it.isNotBlank() } ?: "Unknown title",
                    artist = cursor.getString(artistCol)?.takeIf { it.isNotBlank() && it != "<unknown>" } ?: "Unknown artist",
                    duration = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60),
                    durationSec = seconds,
                    audioUri = uri.toString(),
                    isLiked = uri.toString() in likedIds
                )
            }
        }
        return result
    }
}
