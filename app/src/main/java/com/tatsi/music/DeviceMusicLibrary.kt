package com.tatsi.music
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.tatsi.music.model.Track
import java.util.Locale
import java.util.concurrent.TimeUnit

object DeviceMusicLibrary {
    fun fromUri(context: Context, uri: Uri, likedIds: Set<String>): Track {
        var name = "Imported audio"
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
            if (c.moveToFirst()) name = c.getString(0)?.takeIf { it.isNotBlank() } ?: name
        }
        var title = name.substringBeforeLast(".", name)
        var artist = "Unknown artist"
        var millis = 0L
        val retriever = android.media.MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            title = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_TITLE)?.takeIf { it.isNotBlank() } ?: title
            artist = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST)?.takeIf { it.isNotBlank() && it != "<unknown>" } ?: artist
            millis = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
        } catch (_: Exception) { } finally { runCatching { retriever.release() } }
        val sec = TimeUnit.MILLISECONDS.toSeconds(millis.coerceAtLeast(0L)).toInt()
        val id = uri.toString()
        return Track(id, title.ifBlank { "Imported audio" }, artist, String.format(Locale.getDefault(), "%02d:%02d", sec / 60, sec % 60), sec, id, id in likedIds)
    }
}