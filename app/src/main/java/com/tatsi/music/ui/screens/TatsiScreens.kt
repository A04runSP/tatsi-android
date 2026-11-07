package com.tatsi.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tatsi.music.model.*
import com.tatsi.music.ui.components.*
import com.tatsi.music.ui.theme.LocalTatsiTokens
import com.tatsi.music.ui.theme.tatsiCardSurface

@Composable
fun HomeScreen(
    tracks: List<Track>,
    playlists: List<Playlist>,
    currentTrack: Track,
    isPlaying: Boolean,
    onSelectTrack: (Track) -> Unit,
    onToggleLike: (Track) -> Unit,
    onOpenNowPlaying: () -> Unit,
    onImportMusic: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    val categories = listOf("All", "Playlists", "Liked", "Midnight Edit")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Home",
                style = MaterialTheme.typography.displayLarge,
                color = tokens.textPrimary
            )
            Text(
                text = "An offline music player for those who feel music deeply.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = tokens.textMuted
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isFirst = cat == "All"
                    Surface(
                        shape = CircleShape,
                        color = if (isFirst) tokens.brand else tokens.surface,
                        modifier = Modifier.border(1.dp, tokens.divider, CircleShape)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isFirst) Color(0xFFF9F3EA) else tokens.textMuted,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        item {
            PlaylistBannerCard(
                playlist = playlists.first(),
                onClick = onOpenNowPlaying
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recently Played",
                    style = MaterialTheme.typography.headlineMedium,
                    color = tokens.textPrimary
                )
                Text(
                    text = "See all",
                    style = MaterialTheme.typography.labelSmall,
                    color = tokens.accent
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AlbumArtTile(
                    title = "Chill Vibes",
                    subtitle = "12 songs",
                    motif = MotifType.MISTY_MOUNTAINS,
                    modifier = Modifier.weight(1f),
                    onClick = { tracks.getOrNull(1)?.let(onSelectTrack) }
                )
                AlbumArtTile(
                    title = "My Library",
                    subtitle = "418 songs",
                    motif = MotifType.CHERRY_BLOSSOM,
                    modifier = Modifier.weight(1f),
                    onClick = { tracks.firstOrNull()?.let(onSelectTrack) }
                )
            }
        }

        item {
            Text(
                text = "Recently Added",
                style = MaterialTheme.typography.headlineMedium,
                color = tokens.textPrimary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        items(tracks, key = { it.id }) { track ->
            MusicListRow(
                track = track,
                isPlaying = track.id == currentTrack.id && isPlaying,
                onTrackClick = { onSelectTrack(track) },
                onLikeClick = { onToggleLike(track) }
            )
        }
    }
}

@Composable
fun LibraryScreen(
    tracks: List<Track>,
    currentTrack: Track,
    isPlaying: Boolean,
    onSelectTrack: (Track) -> Unit,
    onToggleLike: (Track) -> Unit,
    onImportMusic: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "My Library",
                style = MaterialTheme.typography.displayLarge,
                color = tokens.textPrimary
            )
            Text(
                text = "${tracks.size} imported tracks",
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.textMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onImportMusic) { Icon(Icons.Default.Add, contentDescription = null); Spacer(Modifier.width(8.dp)); Text("Import Music") }
            if (tracks.isEmpty()) Text("Your library is empty. Import audio files to begin.", color = tokens.textMuted, modifier = Modifier.padding(vertical = 24.dp))
        }

        items(tracks, key = { it.id }) { track ->
            MusicListRow(
                track = track,
                isPlaying = track.id == currentTrack.id && isPlaying,
                onTrackClick = { onSelectTrack(track) },
                onLikeClick = { onToggleLike(track) }
            )
        }
    }
}

@Composable
fun PlaylistsScreen(
    playlists: List<Playlist>,
    onSelectPlaylist: (Playlist) -> Unit
) {
    val tokens = LocalTatsiTokens.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Playlists",
                style = MaterialTheme.typography.displayLarge,
                color = tokens.textPrimary
            )
            Text(
                text = "Artwork-led collections & mood libraries",
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.textMuted
            )
        }

        items(playlists, key = { it.id }) { playlist ->
            PlaylistBannerCard(
                playlist = playlist,
                onClick = { onSelectPlaylist(playlist) }
            )
        }
    }
}

@Composable
fun SettingsScreen(
    currentStyle: TatsiStyle,
    currentMode: TatsiMode,
    onStyleChange: (TatsiStyle) -> Unit,
    onModeChange: (TatsiMode) -> Unit
) {
    val tokens = LocalTatsiTokens.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Appearance & Theme",
                style = MaterialTheme.typography.displayLarge,
                color = tokens.textPrimary
            )
            Text(
                text = "Switch between the default Premium design and the optional Watercolor v1.0 art style.",
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.textMuted
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .tatsiCardSurface(tokens = tokens)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "1. Design System Direction",
                    style = MaterialTheme.typography.headlineMedium,
                    color = tokens.textPrimary
                )
                TatsiStyle.entries.forEach { styleOption ->
                    val selected = styleOption == currentStyle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .tatsiCardSurface(tokens = tokens, isActive = selected, cornerRadius = 14.dp)
                            .clickable { onStyleChange(styleOption) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selected,
                            onClick = { onStyleChange(styleOption) },
                            colors = RadioButtonDefaults.colors(selectedColor = tokens.accent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = styleOption.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = tokens.textPrimary
                            )
                            Text(
                                text = styleOption.subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textMuted
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .tatsiCardSurface(tokens = tokens)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "2. Theme Surface Mode",
                    style = MaterialTheme.typography.headlineMedium,
                    color = tokens.textPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TatsiMode.entries.forEach { modeOption ->
                        val selected = modeOption == currentMode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .tatsiCardSurface(tokens = tokens, isActive = selected, cornerRadius = 14.dp)
                                .clickable { onModeChange(modeOption) }
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = modeOption.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = tokens.textPrimary
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .tatsiCardSurface(tokens = tokens)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Active Palette Tokens",
                    style = MaterialTheme.typography.headlineMedium,
                    color = tokens.textPrimary
                )
                Text(
                    text = tokens.themeName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = tokens.accent
                )

                val chunkedSwatches = tokens.swatches.chunked(3)
                chunkedSwatches.forEach { rowSwatches ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSwatches.forEach { swatch ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(swatch.color)
                                    .border(1.dp, tokens.divider, RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = swatch.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = swatch.onColor,
                                    maxLines = 1
                                )
                                Text(
                                    text = swatch.hex,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = swatch.onColor.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NowPlayingDialog(
    track: Track,
    isPlaying: Boolean,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    onTogglePlay: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onClose: () -> Unit,
    onQuickSwitchStyle: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    val waveformBars = listOf(
        0.3f, 0.55f, 0.4f, 0.75f, 0.95f, 0.6f, 0.45f, 0.85f, 0.7f,
        0.35f, 0.65f, 0.9f, 0.8f, 0.5f, 0.75f, 0.95f, 0.6f, 0.4f,
        0.7f, 0.85f, 0.55f, 0.45f, 0.75f, 0.6f, 0.35f, 0.5f, 0.3f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Collapse",
                    tint = tokens.textPrimary
                )
            }
            Text(
                text = "NOW PLAYING",
                style = MaterialTheme.typography.labelSmall,
                color = tokens.textMuted
            )
            IconButton(onClick = onQuickSwitchStyle) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Switch Theme Style",
                    tint = tokens.accent
                )
            }
        }

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.CenterHorizontally)
                .clip(if (tokens.isWatercolor) RoundedCornerShape(28.dp) else CircleShape)
                .border(
                    width = 2.dp,
                    color = tokens.accent.copy(alpha = 0.55f),
                    shape = if (tokens.isWatercolor) RoundedCornerShape(28.dp) else CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            TatsiArtworkCanvas(
                motif = track.motif,
                showVinylGrooves = !tokens.isWatercolor,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.displayLarge,
                color = tokens.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "An atmospheric listening experience • ${track.artist}",
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.textMuted
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            if (!tokens.isWatercolor) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val activeIndex = (progress * waveformBars.size).toInt()
                    waveformBars.forEachIndexed { idx, ratio ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(ratio)
                                .clip(CircleShape)
                                .background(
                                    if (idx <= activeIndex) tokens.accent else tokens.divider
                                )
                                .clickable {
                                    onProgressChange(idx.toFloat() / waveformBars.size.toFloat())
                                }
                        )
                    }
                }
            } else {
                Slider(
                    value = progress,
                    onValueChange = onProgressChange,
                    colors = SliderDefaults.colors(
                        thumbColor = tokens.brand,
                        activeTrackColor = tokens.brand,
                        inactiveTrackColor = tokens.accent.copy(alpha = 0.35f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            val currentSec = (track.durationSec * progress).toInt()
            val curFormatted = "%02d:%02d".format(currentSec / 60, currentSec % 60)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = curFormatted, style = MaterialTheme.typography.labelSmall, color = tokens.textMuted)
                Text(text = track.duration, style = MaterialTheme.typography.labelSmall, color = tokens.textMuted)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = tokens.textMuted)
            }
            IconButton(onClick = onPrev) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = tokens.textPrimary, modifier = Modifier.size(32.dp))
            }
            Surface(
                shape = CircleShape,
                color = if (tokens.isWatercolor) tokens.brand else tokens.accent,
                modifier = Modifier
                    .size(68.dp)
                    .clickable { onTogglePlay() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = if (tokens.isWatercolor) Color(0xFFF9F3EA) else Color(0xFF160D12),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
            IconButton(onClick = onNext) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = tokens.textPrimary, modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = tokens.textMuted)
            }
        }
    }
}
