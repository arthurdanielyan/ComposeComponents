package com.danielyan.coreCompose.components.switchC

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A custom switch component with enhanced interactivity.
 *
 * Unlike the Material3 [Switch], this implementation allows the thumb
 * to be dragged smoothly, and it scales with a subtle animation during
 * interaction for better visual feedback.
 *
 * @param isChecked Current state of the switch (true = on, false = off).
 * @param onCheckedStateChange Callback invoked when the switch state changes,
 * providing the new boolean value.
 * @param thumbDiameter Diameter of the draggable thumb.
 * @param thumbPadding Space between the thumb and the track edges.
 * @param colors Defines the colors for the switch's thumb and track in
 * checked and unchecked states.
 * @param modifier Modifier applied to the switch container.
 */
@Composable
fun Switch(
    isChecked: Boolean,
    onCheckedStateChange: (Boolean) -> Unit,
    thumbDiameter: Dp = 20.dp,
    thumbPadding: Dp = 4.dp,
    colors: SwitchColors = SwitchColors.default(),
    modifier: Modifier = Modifier,
) {
    val stateController = rememberSwitchStateController(
        thumbDiameter = thumbDiameter,
        onCheckedStateChange = onCheckedStateChange,
        isChecked = isChecked,
    )
    val thumbScale by stateController.thumbScale.collectAsState()

    NoMinimumTouchTargetSize {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .width(SwitchWidthMultiplier * (ThumbScale * thumbDiameter))
                .height(thumbDiameter + 2 * thumbPadding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            stateController.onTap()
                        }
                    )
                }
                .background(
                    if (isChecked) {
                        colors.checkedBackground
                    } else {
                        colors.uncheckedBackground
                    }
                )
                .wrapContentHeight()
                .padding(thumbPadding)
                .onPlaced {
                    it.boundsInWindow()
                    stateController.onParentBoundsReceived(it)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationX = stateController.dragAnimatable.value
                        scaleX = thumbScale
                        scaleY = thumbScale
                    }
                    .clip(CircleShape)
                    .background(colors.thumb)
                    .size(thumbDiameter)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                stateController.onDrag(change, dragAmount)
                            },
                            onDragEnd = {
                                stateController.onDragEnd()
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                stateController.onPress()
                            },
                            onTap = {
                                stateController.onTap()
                            }
                        )
                    }
            )
        }
    }
}

@Composable
private fun rememberSwitchStateController(
    thumbDiameter: Dp,
    onCheckedStateChange: (Boolean) -> Unit,
    isChecked: Boolean,
): SwitchStateController {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val currentIsChecked by rememberUpdatedState(isChecked)

    return remember {
        SwitchStateController(
            density = density,
            thumbDiameter = thumbDiameter,
            onCheckedStateChange = onCheckedStateChange,
            isCheckedState = { currentIsChecked },
            scope = scope,
        )
    }
}

private class SwitchStateController(
    private val density: Density,
    private val thumbDiameter: Dp,
    private val onCheckedStateChange: (Boolean) -> Unit,
    private var isCheckedState: () -> Boolean,
    private val scope: CoroutineScope,
) {

    val dragAnimatable = Animatable(0f).apply {
        updateBounds(lowerBound = 0f)
    }

    private val isDragging = MutableStateFlow(false)
    val thumbScale = isDragging.map {
        if (it) {
            ThumbScale
        } else {
            1f
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000L), 0f)

    var initialState = isCheckedState()

    init {
        observeCheckedState()
    }

    fun onParentBoundsReceived(layoutCoordinates: LayoutCoordinates) {
        dragAnimatable.updateBounds(
            upperBound = layoutCoordinates.size.width.toFloat() - with(density) { thumbDiameter.roundToPx() }
        )
    }

    fun onDrag(change: PointerInputChange, dragAmount: Offset) {
        scope.launch {
            dragAnimatable.snapTo(dragAnimatable.value + dragAmount.x)
            change.consume()
        }
    }

    fun onPress() {
        isDragging.update { true }
        initialState = dragAnimatable.value >= dragAnimatable.boundDiff() * 0.5f
    }

    fun onTap() {
        scope.launch {
            isDragging.update { false }
            if (dragAnimatable.value >= dragAnimatable.boundDiff() * 0.5f) {
                onCheckedStateChange(false)
            } else {
                onCheckedStateChange(true)
            }
        }
    }

    fun onDragEnd() {
        scope.launch {
            isDragging.update { false }
            val isChecked = dragAnimatable.value >= dragAnimatable.boundDiff() * 0.5f
            if (isChecked != initialState) {
                onCheckedStateChange(isChecked)
            }
            settle(isChecked)
        }
    }

    private fun observeCheckedState() {
        scope.launch {
            snapshotFlow { isCheckedState() }
                .collectLatest { isChecked ->
                    settle(isChecked)
                }
        }
    }

    private suspend fun settle(isChecked: Boolean) {
        if (isChecked) {
            dragAnimatable.animateTo(dragAnimatable.upperBound ?: 0f)
        } else {
            dragAnimatable.animateTo(0f)
        }
    }
}

@Composable
private fun NoMinimumTouchTargetSize(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalViewConfiguration provides object :
            ViewConfiguration by LocalViewConfiguration.current {
            override val minimumTouchTargetSize: DpSize = DpSize.Zero
        },
        content = content
    )
}

private fun Animatable<Float, AnimationVector1D>.boundDiff(): Float {
    return (this.upperBound ?: 0f) - (this.lowerBound ?: 0f)
}

private const val SwitchWidthMultiplier = 2.2f
private const val ThumbScale = 1.15f
