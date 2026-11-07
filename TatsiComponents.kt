package com.tatsi.music.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tatsi.music.model.MotifType
import com.tatsi.music.model.Playlist
import com.tatsi.music.model.Track
import com.tatsi.music.ui.theme.LocalTatsiTokens
import com.tatsi.music.ui.theme.tatsiCardSurface

@Composable
fun TatsiArtworkCanvas(
    motif: MotifType,
    modifier: Modifier = Modifier,
    showVinylGrooves: Boolean = false
) {
    val tokens = LocalTatsiTokens.current
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val skyColors = when (motif) {
            MotifType.CHERRY_BLOSSOM -> listOf(Color(0xFF7A2E3A), Color(0xFFC7888E), Color(0xFFF4C7D0))
            MotifType.MISTY_MOUNTAINS -> listOf(Color(0xFF5A2330), Color(0xFFC7888E), Color(0xFFF9F3EA))
            MotifType.CRIMSON_MOON -> listOf(Color(0xFF160D12), Color(0xFF412530), Color(0xFF843B50))
            MotifType.VINYL_ARCHIVE -> listOf(Color(0xFF160D12), Color(0xFF321923), Color(0xFF6F2D40))
        }

        drawRect(brush = Brush.verticalGradient(colors = skyColors))

        drawCircle(
            color = Color(0xFFF9F3EA).copy(alpha = 0.85f),
            radius = w * 0.22f,
            center = Offset(w * 0.68f, h * 0.38f)
        )

        val backMountain = Path().apply {
            moveTo(0f, h * 0.72f)
            quadraticTo(w * 0.28f, h * 0.48f, w * 0.55f, h * 0.68f)
            quadraticTo(w * 0.80f, h * 0.50f, w, h * 0.64f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(backMountain, color = Color(0xFF412530).copy(alpha = 0.65f))

        val frontMountain = Path().apply {
            moveTo(0f, h * 0.82f)
            quadraticTo(w * 0.35f, h * 0.64f, w * 0.72f, h * 0.80f)
            quadraticTo(w * 0.88f, h * 0.72f, w, h * 0.78f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(frontMountain, color = Color(0xFF160D12).copy(alpha = 0.88f))

        if (motif == MotifType.CHERRY_BLOSSOM || motif == MotifType.CRIMSON_MOON || tokens.isWatercolor) {
            drawLine(
                color = Color(0xFF2A171D),
                start = Offset(0f, h * 0.18f),
                end = Offset(w * 0.46f, h * 0.28f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF2A171D),
                start = Offset(w * 0.22f, h * 0.23f),
                end = Offset(w * 0.36f, h * 0.39f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            val blossomPositions = listOf(
                Offset(w * 0.14f, h * 0.20f),
                Offset(w * 0.28f, h * 0.24f),
                Offset(w * 0.44f, h * 0.27f),
                Offset(w * 0.35f, h * 0.38f),
                Offset(w * 0.20f, h * 0.33f)
            )
            blossomPositions.forEachIndexed { idx, pos ->
                drawCircle(
                    color = if (idx % 2 == 0) Color(0xFFF4C7D0) else Color(0xFFF9F3EA),
                    radius = w * 0.035f,
                    center = pos
                )
            }
        }

        if (showVinylGrooves) {
            val center = Offset(w / 2f, h / 2f)
            for (r in 24..88 step 8) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = (w * (r / 200f)),
                    center = center,
                    style = Stroke(width = 1.2f)
                )
            }
        }
    }
}

@Composable
fun MusicListRow(
    track: Track,
    isPlaying: Boolean,
    onTrackClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .tatsiCardSurface(tokens = tokens, isActive = isPlaying, cornerRadius = 16.dp)
            .clickable { onTrackClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            TatsiArtworkCanvas(
                motif = track.motif,
                modifier = Modifier.fillMaxSize()
            )
            if (isPlaying) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Playing",
                    tint = Color(0xFFF9F3EA),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleMedium,
                color = tokens.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = tokens.textMuted,
                maxLines = 1
            )
        }

        Text(
            text = track.duration,
            style = MaterialTheme.typography.labelSmall,
            color = tokens.textMuted,
            modifier = Modifier.padding(end = 8.dp)
        )

        IconButton(
            onClick = onLikeClick,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = if (track.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (track.isLiked) tokens.accent else tokens.textMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun PlaylistBannerCard(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .tatsiCardSurface(tokens = tokens, cornerRadius = 22.dp)
            .clickable { onClick() }
    ) {
        TatsiArtworkCanvas(
            motif = playlist.motif,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF160D12).copy(alpha = 0.80f))
                    )
                )
                .padding(18.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(0xFF160D12).copy(alpha = 0.55f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = if (tokens.isWatercolor) "WATERCOLOR COLLECTION" else "EDITORIAL FEATURE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFF9F3EA),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = playlist.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFFF9F3EA)
                    )
                    Text(
                        text = "${playlist.songCount} songs • ${playlist.subtitle}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFF9F3EA).copy(alpha = 0.85f)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = if (tokens.isWatercolor) tokens.brand else tokens.accent,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Playlist",
                            tint = if (tokens.isWatercolor) Color(0xFFF9F3EA) else Color(0xFF160D12)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumArtTile(
    title: String,
    subtitle: String,
    motif: MotifType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    Column(
        modifier = modifier
            .tatsiCardSurface(tokens = tokens, cornerRadius = 18.dp)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        TatsiArtworkCanvas(
            motif = motif,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.15f)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = tokens.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = tokens.textMuted,
            maxLines = 1
        )
    }
}

@Composable
fun TatsiMiniPlayer(
    currentTrack: Track,
    isPlaying: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onOpenNowPlaying: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = LocalTatsiTokens.current
    val backgroundBrush = if (tokens.isWatercolor) {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFF7A2E3A), Color(0xFF9E4756), Color(0xFFC7888E))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(tokens.cardElevated, tokens.surface)
        )
    }
    val contentColor = if (tokens.isWatercolor) Color(0xFFF9F3EA) else tokens.textPrimary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundBrush)
            .clickable { onOpenNowPlaying() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TatsiArtworkCanvas(
                motif = currentTrack.motif,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(11.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentTrack.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    maxLines = 1
                )
                Text(
                    text = currentTrack.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.78f),
                    maxLines = 1
                )
            }

            IconButton(onClick = onTogglePlay) {
                Surface(
                    shape = CircleShape,
                    color = if (tokens.isWatercolor) Color(0xFFF9F3EA) else tokens.accent,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = if (tokens.isWatercolor) Color(0xFF7A2E3A) else Color(0xFF160D12),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = contentColor
                )
            }
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp),
            color = if (tokens.isWatercolor) Color(0xFFF9F3EA) else tokens.accent,
            trackColor = contentColor.copy(alpha = 0.16f)
        )
    }
}
