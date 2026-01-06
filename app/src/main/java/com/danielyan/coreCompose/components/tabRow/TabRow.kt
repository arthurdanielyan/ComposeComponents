package com.danielyan.coreCompose.components.tabRow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A horizontal tab row composable that works with a [PagerState] to display
 * a scrollable set of tabs with a moving indicator.
 *
 * Features:
 * - Automatically animates the indicator width and position based on the
 *   current page and swipe offset.
 * - Tabs can be clicked to navigate to the corresponding page.
 * - Supports enabling or disabling user scroll gestures on the pager.
 * - Indicator color and thickness can be customized.
 *
 * **Important:** The [Modifier] provided to each tab via the [itemContent] lambda
 * **must be applied** to the root of the tab composable to ensure correct
 * click handling and indicator tracking.
 *
 * @param modifier Modifier applied to the entire tab row container.
 * @param pagerState The [PagerState] that this tab row is linked to, used to
 * determine the current page and animate the indicator.
 * @param items The list of data items representing each tab.
 * @param userScrollEnabled Whether user scrolling is enabled for the pager.
 * Defaults to `true`.
 * @param indicatorColor Color of the tab indicator.
 * @param indicatorThickness Thickness of the tab indicator. Defaults to `1.dp`.
 * @param itemContent A composable lambda to display each tab item. Provides
 * the [Modifier] for the tab, the data item, and a Boolean indicating whether
 * this tab is currently selected. The modifier **must be applied** to the root
 * of the tab content for proper behavior.
 */
@ExperimentalFoundationApi
@Composable
fun <T> TabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    items: List<T>,
    userScrollEnabled: Boolean = true,
    indicatorColor: Color = MaterialTheme.colorScheme.onBackground,
    indicatorThickness: Dp = 1.dp,
    itemContent: @Composable RowScope. (Modifier, T, Boolean) -> Unit
) {
    val stateController = rememberTabRowStateController(
        pagerState = pagerState,
        userScrollEnabled = userScrollEnabled,
    )
    val indicatorWidthScale by stateController.indicatorWidthScale.collectAsState()
    val indicatorOffset by stateController.indicatorOffset.collectAsState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    val itemModifier = Modifier
                        .clickable {
                            stateController.onItemClick(index)
                        }
                        .onPlaced {
                            stateController.onItemBoundsReceived(
                                itemIndex = index,
                                layoutCoordinates = it
                            )
                        }
                    itemContent(
                        itemModifier,
                        item,
                        pagerState.settledPage == index,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(1.px())
                    .height(indicatorThickness)
                    .graphicsLayer {
                        scaleX = indicatorWidthScale
                        transformOrigin = TransformOrigin(0f, 0.5f)
                        translationX = indicatorOffset
                    }
                    .background(indicatorColor)
            )
        }
    }
}

@Composable
private fun Int.px(): Dp {
    val density = LocalDensity.current
    return remember(density) {
        with(density) {
            this@px.toDp()
        }
    }
}
