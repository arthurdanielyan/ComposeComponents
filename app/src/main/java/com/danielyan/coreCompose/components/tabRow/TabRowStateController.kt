package com.danielyan.coreCompose.components.tabRow

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private class TabBounds(
    val width: Int = 0,
    val offset: Int = 0,
)

@Immutable
internal class TabRowStateController(
    private val pagerState: PagerState,
    private val scope: CoroutineScope,
    private val userScrollEnabled: Boolean,
) {
    private companion object {
        private const val PageSettleDuration = 200
    }

    private var tabBounds: Array<TabBounds> = Array(pagerState.pageCount) {
        TabBounds()
    }
    private val refreshIndicatorParamsEvent = MutableSharedFlow<Unit>(
        replay = 1
    )

    val indicatorOffset = refreshIndicatorParamsEvent.flatMapLatest {
        snapshotFlow { pagerState.currentPageOffsetFraction }
    }.mapLatest { fraction ->
        calculateIndicatorOffset(fraction, pagerState.currentPage)
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000L), 0f)

    val indicatorWidthScale = refreshIndicatorParamsEvent.flatMapLatest {
        snapshotFlow { pagerState.currentPageOffsetFraction }
    }.mapLatest { fraction ->

        val currentBounds = tabBounds[pagerState.currentPage]
        val progress = if (fraction >= 0) {
            fraction
        } else {
            fraction.absoluteValue.reverseProgress()
        }
        if (fraction > 0) {
            val targetBounds = tabBounds[pagerState.currentPage + 1]
            currentBounds.width + progress * (targetBounds.width - currentBounds.width)
        } else if (fraction < 0) {
            val targetBounds = tabBounds[pagerState.currentPage - 1]
            currentBounds.width + progress.reverseProgress() * (targetBounds.width - currentBounds.width)
        } else { // fraction == 0
            tabBounds[pagerState.currentPage].width.toFloat()
        }
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000L), 0f)


    fun onItemBoundsReceived(
        itemIndex: Int,
        layoutCoordinates: LayoutCoordinates
    ) {
        tabBounds[itemIndex] = TabBounds(
            width = layoutCoordinates.size.width,
            offset = layoutCoordinates.positionInParent().x.roundToInt()
        )
        refreshIndicatorParamsEvent.tryEmit(Unit)
    }

    fun onItemClick(index: Int) {
        if (userScrollEnabled) {
            scope.launch {
                pagerState.animateScrollToPage(
                    page = index,
                    animationSpec = tween(
                        durationMillis = PageSettleDuration,
                        easing = LinearEasing
                    )
                )
            }
        }
    }

    private fun calculateIndicatorOffset(
        fraction: Float,
        currentPage: Int
    ): Float {

        val currentBounds = tabBounds[currentPage]
        return if (fraction > 0) {
            val targetBounds = tabBounds[currentPage + 1]
            val diff = targetBounds.offset - currentBounds.offset
            val progress = fraction
            currentBounds.offset + progress * diff
        } else if (fraction < 0) {
            val targetBounds = tabBounds[currentPage - 1]
            val diff = currentBounds.offset - targetBounds.offset
            val progress = fraction.absoluteValue.reverseProgress()
            targetBounds.offset + progress * diff
        } else { // fraction == 0
            tabBounds[currentPage].offset.toFloat()
        }
    }

    private fun Float.reverseProgress(): Float {
        require(this in 0f..1f)
        return 1f - this
    }
}

@Composable
internal fun rememberTabRowStateController(
    pagerState: PagerState,
    userScrollEnabled: Boolean,
): TabRowStateController {
    val scope = rememberCoroutineScope()
    return remember {
        TabRowStateController(
            pagerState = pagerState,
            scope = scope,
            userScrollEnabled = userScrollEnabled,
        )
    }
}
