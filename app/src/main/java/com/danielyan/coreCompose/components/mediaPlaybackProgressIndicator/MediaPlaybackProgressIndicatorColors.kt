package com.danielyan.coreCompose.components.mediaPlaybackProgressIndicator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MediaPlaybackProgressIndicatorColors(
    val inactiveTrack: Color,
    val activeTrack: Color,
    val thumb: Color,
) {

    companion object {

        @Composable
        fun default(
            backgroundLine: Color = MaterialTheme.colorScheme.surfaceVariant,
            trackedLineColor: Color = MaterialTheme.colorScheme.primary,
            thumb: Color = MaterialTheme.colorScheme.primary,
        ): MediaPlaybackProgressIndicatorColors {
            return MediaPlaybackProgressIndicatorColors(
                inactiveTrack = backgroundLine,
                activeTrack = trackedLineColor,
                thumb = thumb,
            )
        }
    }
}
