package com.example.sokasinging.presentation.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.media3.common.Player
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(UnstableApi::class)
@Composable
    fun ExoMediaPlayer(songTitle: String, songUrl: String) {
        val context = LocalContext.current

        // Create player with proper error handling
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("ExoMediaPlayer", "Player error: ${error.message}")
                        // Handle error appropriately
                    }
                })
            }
        }

        var isPlaying by remember { mutableStateOf(false) }
        var currentPosition by remember { mutableStateOf(0L) }
        var duration by remember { mutableStateOf(0L) }

        // Set media item when composable first loads or URL changes
        LaunchedEffect(songUrl) {
            try {
                Log.d("ExoMediaPlayer", "Setting media item with URL: $songUrl")
                exoPlayer.setMediaItem(MediaItem.fromUri(songUrl))
                exoPlayer.prepare()
                Log.d("ExoMediaPlayer", "Media item prepared successfully")
            } catch (e: Exception) {
                Log.e("ExoMediaPlayer", "Failed to load media: ${e.message}")
                Log.e("ExoMediaPlayer", "Exception: ${e.stackTraceToString()}")
            }
        }

        // Update position periodically using a safer approach
        LaunchedEffect(Unit) {
            while (isActive) { // isActive is a property of the coroutine scope
                currentPosition = exoPlayer.currentPosition
                duration = exoPlayer.duration.takeIf { it > 0 } ?: 0L
                delay(500)
            }
        }

        // Handle lifecycle properly
        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(12.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = songTitle, style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = if (duration > 0) currentPosition.toFloat() / duration else 0f,
                onValueChange = { newValue ->
                    val seekPosition = (newValue * duration).toLong()
                    exoPlayer.seekTo(seekPosition)
                    currentPosition = seekPosition
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = formatTime(duration), style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Play/Pause Button
            IconButton(
                onClick = {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        exoPlayer.play()
                    } else {
                        exoPlayer.pause()
                    }
                }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

    }

fun formatTime(ms: Long): String {
    val minutes = (ms / 1000) / 60
    val seconds = (ms / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}



