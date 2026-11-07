package com.tatsi.music

import android.os.Bundle
import android.content.ComponentName
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tatsi.music.model.*
import com.tatsi.music.ui.components.TatsiMiniPlayer
import com.tatsi.music.ui.screens.*
import com.tatsi.music.ui.theme.LocalTatsiTokens
import com.tatsi.music.ui.theme.TatsiTheme
import com.tatsi.music.ui.theme.tatsiScreenBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.await

enum class AppTab(val label: String) {
    HOME("Home"),
    LIBRARY("Library"),
    PLAYLISTS("Playlists"),
    SETTINGS("Settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Launches in Primary Default Theme: PREMIUM (Midnight Burgundy Dark)
            var currentStyle by remember { mutableStateOf(TatsiStyle.PREMIUM) }
            var currentMode by remember { mutableStateOf(TatsiMode.DARK) }

            TatsiTheme(style = currentStyle, mode = currentMode) {
                TatsiMainApp(
                    currentStyle = currentStyle,
                    currentMode = currentMode,
                    onStyleChange = { currentStyle = it },
                    onModeChange = { currentMode = it }
                )
            }
        }
    }
}

@Composable
fun TatsiMainApp(
    currentStyle: TatsiStyle,
    currentMode: TatsiMode,
    onStyleChange: (TatsiStyle) -> Unit,
    onModeChange: (TatsiMode) -> Unit
) {
    val tokens = LocalTatsiTokens.current
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(AppTab.HOME) }
    var tracks by remember { mutableStateOf(emptyList<Track>()) }
    val likedPrefs = remember(context) { context.getSharedPreferences("tatsi_liked_tracks", android.content.Context.MODE_PRIVATE) }
    val playlists = remember { TatsiRepository.playlists }

    var currentTrackIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.41f) }
    var isNowPlayingOpen by remember { mutableStateOf(false) }

    val currentTrack = tracks.getOrNull(currentTrackIndex) ?: TatsiRepository.tracks.first()
    val lifecycleOwner = LocalLifecycleOwner.current
    var mediaController by remember { mutableStateOf<MediaController?>(null) }
    var controllerFuture by remember { mutableStateOf<ListenableFuture<MediaController>?>(null) }
    val importAudioLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        val imported = uris.mapNotNull { uri ->
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                DeviceMusicLibrary.fromUri(context, uri, likedPrefs.getStringSet("liked_ids", emptySet<String>())?.toSet() ?: emptySet())
            }.getOrNull()
        }
        if (imported.isNotEmpty()) {
            val prefs = context.getSharedPreferences("tatsi_imported_audio", android.content.Context.MODE_PRIVATE)
            val saved = prefs.getStringSet("uris", emptySet<String>())?.toMutableSet() ?: mutableSetOf()
            imported.forEach { saved.add(it.id) }
            prefs.edit().putStringSet("uris", saved).apply()
            val known = tracks.map { it.id }.toSet()
            tracks = tracks + imported.filterNot { it.id in known }
        }
    }
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("tatsi_imported_audio", android.content.Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("uris", emptySet<String>())?.toSet() ?: emptySet()
        val likes = likedPrefs.getStringSet("liked_ids", emptySet<String>())?.toSet() ?: emptySet()
        tracks = saved.mapNotNull { raw -> runCatching { DeviceMusicLibrary.fromUri(context, Uri.parse(raw), likes) }.getOrNull() }
    }
    // Connect the UI to the service-owned player. The service remains alive beyond Activity recreation.
    LaunchedEffect(context) {
        val token = SessionToken(context, ComponentName(context, com.tatsi.music.playback.TatsiPlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        controllerFuture = future
        try { mediaController = future.await() } catch (_: Exception) { /* UI remains usable if service connection fails */ }
    }
    DisposableEffect(lifecycleOwner, mediaController) {
        val controller = mediaController
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) { isPlaying = playing }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val id = mediaItem?.mediaId
                val index = tracks.indexOfFirst { it.id == id }
                if (index >= 0) currentTrackIndex = index
                progress = 0f
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                val duration = controller?.duration ?: 0L
                if (duration > 0L) progress = (controller?.currentPosition?.toFloat() ?: 0f) / duration.toFloat()
            }
        }
        controller?.addListener(listener)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val c = mediaController
                isPlaying = c?.isPlaying == true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { controller?.removeListener(listener); lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(isPlaying, mediaController) {
        while (isPlaying && mediaController != null) {
            val c = mediaController
            val duration = c?.duration ?: 0L
            val position = c?.currentPosition ?: 0L
            if (duration > 0L) progress = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            delay(400L)
        }
    }

    fun playTrack(index: Int) {
        if (tracks.isEmpty()) return
        currentTrackIndex = index.coerceIn(0, tracks.lastIndex)
        val track = tracks[currentTrackIndex]
        val uri = track.audioUri
        val controller = mediaController
        if (controller != null && !uri.isNullOrBlank()) {
            controller.setMediaItems(tracks.map { t -> MediaItem.Builder().setMediaId(t.id).setUri(Uri.parse(t.audioUri!!)).setMediaMetadata(androidx.media3.common.MediaMetadata.Builder().setTitle(t.title).setArtist(t.artist).build()).build() }, currentTrackIndex, 0L)
            controller.prepare(); controller.play(); progress = 0f
        } else {
            isPlaying = false
        }
    }
    fun skipTo(index: Int) {
        if (tracks.isEmpty()) return
        val nextIndex = (index + tracks.size) % tracks.size
        playTrack(nextIndex)
    }
    fun togglePlayback() {
        val c = mediaController
        if (c != null && c.currentMediaItem != null) { if (c.isPlaying) c.pause() else c.play() }
        else playTrack(currentTrackIndex)
    }

    DisposableEffect(Unit) {
        onDispose { controllerFuture?.let { MediaController.releaseFuture(it) } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .tatsiScreenBackground(tokens)
    ) {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TatsiTopHeader(
                    onQuickToggleWatercolor = {
                        if (currentStyle == TatsiStyle.PREMIUM) {
                            onStyleChange(TatsiStyle.WATERCOLOR)
                            onModeChange(TatsiMode.LIGHT)
                        } else {
                            onStyleChange(TatsiStyle.PREMIUM)
                            onModeChange(TatsiMode.DARK)
                        }
                    },
                    onOpenSettings = { activeTab = AppTab.SETTINGS }
                )
            },
            bottomBar = {
                Column {
                    TatsiMiniPlayer(
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        progress = progress,
                        onTogglePlay = { togglePlayback() },
                        onNext = { skipTo(currentTrackIndex + 1) },
                        onOpenNowPlaying = { isNowPlayingOpen = true }
                    )

                    NavigationBar(
                        containerColor = tokens.surface.copy(alpha = 0.95f),
                        tonalElevation = 0.dp
                    ) {
                        AppTab.entries.forEach { tab ->
                            val selected = activeTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { activeTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = when (tab) {
                                            AppTab.HOME -> Icons.Default.Home
                                            AppTab.LIBRARY -> Icons.Default.LibraryMusic
                                            AppTab.PLAYLISTS -> Icons.Default.QueueMusic
                                            AppTab.SETTINGS -> Icons.Default.Settings
                                        },
                                        contentDescription = tab.label
                                    )
                                },
                                label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = tokens.accent,
                                    selectedTextColor = tokens.accent,
                                    unselectedIconColor = tokens.textMuted,
                                    unselectedTextColor = tokens.textMuted,
                                    indicatorColor = tokens.cardElevated
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (activeTab) {
                    AppTab.HOME -> HomeScreen(
                        tracks = tracks,
                        playlists = playlists,
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        onSelectTrack = { selected -> playTrack(tracks.indexOfFirst { it.id == selected.id }.coerceAtLeast(0)) },
                        onImportMusic = { importAudioLauncher.launch(arrayOf("audio/*")) },
                        onToggleLike = { target ->
                            tracks = tracks.map { if (it.id == target.id) it.copy(isLiked = !it.isLiked) else it }
                            likedPrefs.edit().putStringSet("liked_ids", tracks.filter { it.isLiked }.map { it.id }.toSet()).apply()
                        },
                        onOpenNowPlaying = { isNowPlayingOpen = true }
                    )

                    AppTab.LIBRARY -> LibraryScreen(
                        tracks = tracks,
                        currentTrack = currentTrack,
                        isPlaying = isPlaying,
                        onSelectTrack = { selected ->
                            playTrack(tracks.indexOfFirst { it.id == selected.id }.coerceAtLeast(0))
                        },
                        onToggleLike = { target ->
                            tracks = tracks.map { if (it.id == target.id) it.copy(isLiked = !it.isLiked) else it }
                            likedPrefs.edit().putStringSet("liked_ids", tracks.filter { it.isLiked }.map { it.id }.toSet()).apply()
                        }
                    )

                    AppTab.PLAYLISTS -> PlaylistsScreen(
                        playlists = playlists,
                        onSelectPlaylist = {
                            isNowPlayingOpen = true
                            playTrack(currentTrackIndex)
                        }
                    )

                    AppTab.SETTINGS -> SettingsScreen(
                        currentStyle = currentStyle,
                        currentMode = currentMode,
                        onStyleChange = onStyleChange,
                        onModeChange = onModeChange
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isNowPlayingOpen,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .tatsiScreenBackground(tokens)
            ) {
                NowPlayingDialog(
                    track = currentTrack,
                    isPlaying = isPlaying,
                    progress = progress,
                    onProgressChange = { value ->
                        progress = value.coerceIn(0f, 1f)
                        val c = mediaController
                        if (c != null && c.duration > 0L) c.seekTo((c.duration * progress).toLong())
                    },
                    onTogglePlay = { togglePlayback() },
                    onPrev = { val c = mediaController; if (c != null) { if (c.currentPosition > 0L) c.seekTo(0L) else if (c.hasPreviousMediaItem()) c.seekToPreviousMediaItem() } },
                    onNext = { mediaController?.seekToNextMediaItem() ?: skipTo(currentTrackIndex + 1) },
                    onClose = { isNowPlayingOpen = false },
                    onQuickSwitchStyle = {
                        val nextStyle = if (currentStyle == TatsiStyle.PREMIUM) TatsiStyle.WATERCOLOR else TatsiStyle.PREMIUM
                        onStyleChange(nextStyle)
                    }
                )
            }
        }
    }
}

@Composable
fun TatsiTopHeader(
    onQuickToggleWatercolor: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val tokens = LocalTatsiTokens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "TATSI",
                    style = MaterialTheme.typography.headlineLarge,
                    color = tokens.textPrimary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = tokens.accent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = tokens.tagline,
                style = MaterialTheme.typography.labelSmall,
                fontStyle = if (tokens.isWatercolor) FontStyle.Italic else FontStyle.Normal,
                color = tokens.textMuted
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                shape = CircleShape,
                color = tokens.surface,
                modifier = Modifier.border(1.dp, tokens.divider, CircleShape)
            ) {
                TextButton(
                    onClick = onQuickToggleWatercolor,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = "Switch Theme",
                        tint = tokens.accent,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (tokens.isWatercolor) "WATERCOLOR v1.0" else "MIDNIGHT BURGUNDY",
                        style = MaterialTheme.typography.labelSmall,
                        color = tokens.textPrimary,
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .size(36.dp)
                    .background(tokens.surface, CircleShape)
                    .border(1.dp, tokens.divider, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Theme Settings",
                    tint = tokens.textPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
