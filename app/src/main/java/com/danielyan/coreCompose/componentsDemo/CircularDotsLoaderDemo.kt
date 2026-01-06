package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.CircularDotsLoader


@Composable
fun CircularDotsLoaderDemo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        )
    ) {
        Text(
            text = "CircularDotsLoader",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            horizontalArrangement = Arrangement
                .spacedBy(16.dp)
        ) {
            CircularDotsLoader()
            CircularDotsLoader(
                dotsCount = 6,
                animationDurationMillis = 1500
            )
            CircularDotsLoader(
                dotsCount = 4,
                animationDurationMillis = 700
            )
        }
    }
}