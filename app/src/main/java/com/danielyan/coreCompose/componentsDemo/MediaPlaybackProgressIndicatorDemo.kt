package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.mediaPlaybackProgressIndicator.MediaPlaybackProgressIndicator
import kotlinx.coroutines.delay

@Composable
internal fun MediaPlaybackProgressIndicatorDemo() {

    var progress = rememberSaveable {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {
        while (true) {
            if(progress.value >= 1f) {
                progress.value = 0f
            }
            delay(1000)
            progress.value += 0.01f
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        )
    ) {
        Text(
            text = "MediaPlaybackProgressIndicator",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        MediaPlaybackProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = progress,
            onProgressChange = {
                progress.value = it
            }
        )
    }
}