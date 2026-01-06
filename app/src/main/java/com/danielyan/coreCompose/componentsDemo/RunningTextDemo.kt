package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.RunningText

@Composable
fun RunningTextDemo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        )
    ) {
        var isLongText by remember {
            mutableStateOf(true)
        }
        Text(
            text = "Running text",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        RunningText(
            text = if(isLongText) {
                LongText
            } else {
                ShortText
            },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Button(
            onClick = {
                isLongText = !isLongText
            }
        ) {
            Text(
                text = "Toggle text length",
            )
        }
    }
}

private const val LongText = "Matthew, Mark, Luke, John, Acts, Romans, 1 Corinthians"
private const val ShortText = "Matthew"
