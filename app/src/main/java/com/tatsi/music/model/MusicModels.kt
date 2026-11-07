package com.tatsi.music.model

import androidx.compose.ui.graphics.Color

enum class TatsiStyle(val title: String, val subtitle: String) {
    PREMIUM(
        title = "Premium Editorial (Default)",
        subtitle = "Midnight Burgundy & Rose Paper with waveforms and vinyl grooves"
    ),
    WATERCOLOR(
        title = "Watercolor Art Style (v1.0)",
        subtitle = "Paper wash, pigment bloom, brush edges & botanical motifs"
    )
}

enum class TatsiMode(val label: String) {
    DARK("Dark Theme"),
    LIGHT("Light Theme")
}

enum class MotifType {
    CHERRY_BLOSSOM,
    MISTY_MOUNTAINS,
    CRIMSON_MOON,
    VINYL_ARCHIVE
}

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val durationSec: Int,
    /** Audio source URI (content:// or https://); null for metadata-only demo tracks. */
    val audioUri: String? = null,
    val isLiked: Boolean = false,
    val motif: MotifType = MotifType.CRIMSON_MOON,
    val primaryTint: Color = Color(0xFF843B50),
    val secondaryTint: Color = Color(0xFFC17A83)
)

data class Playlist(
    val id: String,
    val title: String,
    val songCount: Int,
    val subtitle: String,
    val motif: MotifType,
    val waveformHeights: List<Float>
)

object TatsiRepository {
    val tracks = listOf(
        Track(
            id = "1",
            title = "Midnight Echoes",
            artist = "The Quiet Hours",
            duration = "04:08",
            durationSec = 248,
            isLiked = true,
            motif = MotifType.CRIMSON_MOON,
            primaryTint = Color(0xFF843B50),
            secondaryTint = Color(0xFFC17A83)
        ),
        Track(
            id = "2",
            title = "Another Life",
            artist = "Mira",
            duration = "04:12",
            durationSec = 252,
            isLiked = true,
            motif = MotifType.CHERRY_BLOSSOM,
            primaryTint = Color(0xFF7A2E3A),
            secondaryTint = Color(0xFFF4C7D0)
        ),
        Track(
            id = "3",
            title = "Body Beat",
            artist = "Nya",
            duration = "03:29",
            durationSec = 209,
            isLiked = false,
            motif = MotifType.MISTY_MOUNTAINS,
            primaryTint = Color(0xFFC7888E),
            secondaryTint = Color(0xFFA4B8A0)
        ),
        Track(
            id = "4",
            title = "One Call Away",
            artist = "Cloud Nine",
            duration = "03:54",
            durationSec = 234,
            isLiked = true,
            motif = MotifType.VINYL_ARCHIVE,
            primaryTint = Color(0xFF412530),
            secondaryTint = Color(0xFFD69AA0)
        ),
        Track(
            id = "5",
            title = "Piel Con Piel",
            artist = "La Santa Grifa",
            duration = "03:42",
            durationSec = 222,
            isLiked = false,
            motif = MotifType.CRIMSON_MOON,
            primaryTint = Color(0xFF6F2D40),
            secondaryTint = Color(0xFFA95667)
        )
    )

    val playlists = listOf(
        Playlist(
            id = "p1",
            title = "Midnight Confessions",
            songCount = 18,
            subtitle = "Editorial discovery • Late night sessions",
            motif = MotifType.CRIMSON_MOON,
            waveformHeights = listOf(0.3f, 0.6f, 0.45f, 0.85f, 0.95f, 0.55f, 0.4f, 0.8f, 0.7f, 0.35f, 0.65f, 0.9f, 0.5f, 0.75f, 0.4f, 0.6f, 0.3f)
        ),
        Playlist(
            id = "p2",
            title = "Chill Vibes",
            songCount = 12,
            subtitle = "Curated collection with watercolor wash",
            motif = MotifType.MISTY_MOUNTAINS,
            waveformHeights = listOf(0.25f, 0.4f, 0.55f, 0.45f, 0.7f, 0.6f, 0.35f, 0.5f, 0.65f, 0.4f, 0.3f, 0.55f, 0.45f, 0.35f, 0.25f)
        ),
        Playlist(
            id = "p3",
            title = "The Crimson Edit",
            songCount = 24,
            subtitle = "Album-art editorial archive",
            motif = MotifType.CHERRY_BLOSSOM,
            waveformHeights = listOf(0.4f, 0.75f, 0.6f, 0.9f, 0.5f, 0.8f, 0.65f, 0.45f, 0.85f, 0.7f, 0.55f, 0.95f, 0.6f, 0.4f, 0.5f)
        )
    )
}
