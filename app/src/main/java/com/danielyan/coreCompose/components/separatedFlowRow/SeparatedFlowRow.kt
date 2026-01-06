package com.danielyan.coreCompose.components.separatedFlowRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import com.danielyan.coreCompose.components.separatedFlowRow.SeparatedFlowRowMeasurePolicy.measure

/**
 * A composable that lays out its children in a horizontal flow, wrapping
 * to new rows as needed, and inserts a custom separator between items.
 *
 * This works like a standard [FlowRow], with the main difference being
 * the ability to add a custom composable separator between items.
 *
 * Additional features:
 * - Control horizontal and vertical arrangement for rows and items
 * - Align items vertically within each row
 *
 * @param modifier Modifier applied to the entire flow row.
 * @param horizontalArrangement Defines horizontal spacing and alignment between items in a row.
 * Defaults to [Arrangement.Start].
 * @param verticalArrangement Defines vertical spacing and alignment between rows.
 * Defaults to [Arrangement.Top].
 * @param itemVerticalAlignment Controls vertical alignment of each item within its row.
 * Defaults to [Alignment.Top].
 * @param itemSeparator A composable to be inserted between items as a separator.
 * Defaults to [VerticalDivider].
 * @param content The content of the flow row; all children will be laid out in sequence with
 * separators applied.
 */
@Composable
fun SeparatedFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.Top,
    itemSeparator: @Composable () -> Unit = { VerticalDivider() },
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier,
        measurePolicy = { constraints ->
            measure(
                constraints = constraints,
                horizontalArrangement = horizontalArrangement,
                verticalArrangement = verticalArrangement,
                itemVerticalAlignment = itemVerticalAlignment,
                separator = itemSeparator,
                content = content,
            )
        }
    )
}
