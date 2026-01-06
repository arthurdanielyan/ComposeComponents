package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.bottomSheet.ModalBottomSheet

@Composable
internal fun BottomSheetDemo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp
        )
    ) {
        var isUnscrollableBottomSheetVisible by remember {
            mutableStateOf(false)
        }
        var isScrollableBottomSheetVisible by remember {
            mutableStateOf(false)
        }
        Text(
            text = "BottomSheet",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            onClick = {
                isUnscrollableBottomSheetVisible = true
            }
        ) {
            Text(
                text = "Show unscrollable bottom sheet"
            )
        }
        Button(
            onClick = {
                isScrollableBottomSheetVisible = true
            }
        ) {
            Text(
                text = "Show scrollable bottom sheet"
            )
        }

        if(isUnscrollableBottomSheetVisible) {
            UnscrollableBottomSheet(
                onDismissRequest = {
                    isUnscrollableBottomSheetVisible = false
                }
            )
        }
        if(isScrollableBottomSheetVisible) {
            ScrollableBottomSheet(
                onDismissRequest = {
                    isScrollableBottomSheetVisible = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnscrollableBottomSheet(
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large),
        onDismissRequest = onDismissRequest,
    ) {
        Column {
            repeat(5) {
                BottomSheetOptionButton(
                    text = "Action $it",
                    onClick = {},
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScrollableBottomSheet(
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large),
        onDismissRequest = onDismissRequest,
    ) {
        LazyColumn {
            items(500) {
                BottomSheetOptionButton(
                    text = "Action $it",
                    onClick = {},
                )
            }
        }
    }
}

@Composable
fun BottomSheetOptionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text
        )
    }
}
