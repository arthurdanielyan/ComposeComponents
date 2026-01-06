package com.danielyan.coreCompose.components.bottomSheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * A custom implementation of [ModalBottomSheet] based on Material3.
 *
 * This composable is largely adapted from the official Material3
 * `ModalBottomSheet`, with the following improvements:
 *
 * - Fixes overly aggressive fling behavior when dragging small sheets
 * - Adjusts nested scroll handling for sheets containing scrollable content
 * - Animates the scrim opacity based on the sheet's visible height fraction
 * - Supports custom padding from the bottom and sides of the screen
 *
 * These changes improve gesture stability, visual feedback, and
 * layout flexibility, especially for compact or partially expanded sheets.
 *
 * @param modifier Modifier applied to the bottom sheet container.
 * @param onDismissRequest Callback invoked when the sheet should be dismissed,
 * typically due to outside taps or back press.
 * @param properties [ModalBottomSheetProperties] for further customization of this modal bottom sheet's window behavior.
 * @param dragHandle Optional composable displayed at the top of the sheet
 * to indicate that it can be dragged.
 * @param content The content displayed inside the bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    dragHandle: @Composable () -> Unit = { DragHandle() },
    content: @Composable () -> Unit,
) {
    val bottomSheetController = rememberBottomSheetController()

    val scope = rememberCoroutineScope()
    val hide: (() -> Unit) = remember {
        {
            scope.launch {
                bottomSheetController.anchoredDraggableState.animateTo(BottomSheetState.Hidden)
                onDismissRequest()
            }
        }
    }

    ModalBottomSheetDialog(
        onDismissRequest = hide,
        properties = properties,
    ) {
        ModalBottomSheetContent(
            modifier = modifier,
            onDismissRequest = hide,
            content = content,
            dragHandle = dragHandle,
            bottomSheetController = bottomSheetController,
        )
    }
}

@Composable
private fun DragHandle() {
    Box(
        Modifier
            .padding(vertical = 4.dp)
            .requiredWidth(DragHandleWidth)
            .requiredHeight(DragHandleHeight)
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            )
    )
}

private val DragHandleWidth = 60.dp
private val DragHandleHeight = 4.dp
