package com.danielyan.coreCompose.components.separatedFlowRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed

internal object SeparatedFlowRowMeasurePolicy {

    fun SubcomposeMeasureScope.measure(
        constraints: Constraints,
        horizontalArrangement: Arrangement.Horizontal,
        verticalArrangement: Arrangement.Vertical,
        itemVerticalAlignment: Alignment.Vertical,
        separator: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ): MeasureResult {
        val placeables = subcompose("content") {
            content()
        }.map {
            it.measure(
                constraints.copy(
                    minWidth = 0,
                )
            )
        }

        val separatorWidth = getSeparatorWidth(
            constraints = constraints,
            separator = separator
        )

        val rows = buildItemsRows(
            placeables = placeables.toMutableList(),
            separatorWidth = separatorWidth,
            horizontalArrangement = horizontalArrangement,
            constraints = constraints,
        )

        addSeparators(
            rows = rows,
            constraints = constraints,
            itemSeparator = separator,
        )


        return layout(
            width = constraints.maxWidth,
            height = calculateLayoutHeight(
                rows = rows,
                verticalArrangement = verticalArrangement,
            )
        ) {
            var yPosition = 0
            rows.fastForEach { row ->
                var xPosition = 0
                val outPositionsX = IntArray(row.size)
                with(horizontalArrangement) {
                    this@layout.arrange(
                        totalSize = constraints.maxWidth,
                        sizes = row.map { it.placeable.width }.toIntArray(),
                        layoutDirection = layoutDirection,
                        outPositions = outPositionsX
                    )
                }

                val rowHeight = row.maxOfOrNull { it.placeable.height } ?: 0
                row.fastForEachIndexed { index, rowItem ->
                    val placeable = rowItem.placeable

                    val alignmentOffset = when (rowItem) {
                        is RowItem.Separator ->
                            (rowHeight - placeable.height) / 2

                        is RowItem.Content ->
                            itemVerticalAlignment.align(
                                size = placeable.height,
                                space = rowHeight
                            )
                    }
                    placeable.place(
                        x = outPositionsX[index],
                        y = yPosition + alignmentOffset,
                    )
                    xPosition += placeable.width
                }
                yPosition += rowHeight + verticalArrangement.spacing.roundToPx()
            }
        }
    }

    private fun SubcomposeMeasureScope.getSeparatorWidth(
        constraints: Constraints,
        separator: @Composable () -> Unit,
    ): Int {
        return subcompose("divider") {
            separator()
        }.map {
            it.measure(constraints.copy(minWidth = 0))
        }[0].width
    }

    private fun MeasureScope.buildItemsRows(
        placeables: MutableList<Placeable>,
        separatorWidth: Int,
        horizontalArrangement: Arrangement.Horizontal,
        constraints: Constraints,
    ): MutableList<MutableList<RowItem>> {
        val rows = mutableListOf<MutableList<RowItem>>()
        var currentRow = mutableListOf<RowItem>()
        var currentRowW = 0
        placeables.fastForEachIndexed { index, placeable ->
            if (index == 0 || currentRowW + placeable.width + separatorWidth + 2 * horizontalArrangement.spacing.roundToPx() <= constraints.maxWidth) {
                if (index != 0) {
                    currentRowW += separatorWidth
                }
                currentRow.add(RowItem.Content(placeable))
                currentRowW += placeable.width + separatorWidth + 2 * horizontalArrangement.spacing.roundToPx()
            } else {
                rows.add(currentRow)
                currentRow = mutableListOf(RowItem.Content(placeable))
                currentRowW = placeable.width
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        return rows
    }

    private fun SubcomposeMeasureScope.addSeparators(
        rows: MutableList<MutableList<RowItem>>,
        constraints: Constraints,
        itemSeparator: @Composable () -> Unit
    ) {
        for (i in rows.indices) {
            val row = rows[i]
            var index = 1
            val separatorCount = row.size - 1
            repeat(separatorCount) {
                row.add(
                    index = index,
                    element = subcompose("separator_${i}_$index") {
                        itemSeparator()
                    }.map { separatorPlaceable ->
                        separatorPlaceable.measure(
                            constraints.copy(
                                minWidth = 0,
                                maxHeight = row.maxOfOrNull { it.placeable.height } ?: 0
                            )
                        )
                    }[0].let { RowItem.Separator(it) }
                )
                index += 2
            }
        }
    }

    private fun MeasureScope.calculateLayoutHeight(
        rows: List<List<RowItem>>,
        verticalArrangement: Arrangement.Vertical,
    ): Int {
        return rows.sumOf { row ->
            (row.maxOfOrNull { it.placeable.height } ?: 0)
        } + verticalArrangement.spacing.roundToPx() * (rows.size - 1)
    }
}

private sealed interface RowItem {
    val placeable: Placeable

    data class Content(
        override val placeable: Placeable
    ) : RowItem

    data class Separator(
        override val placeable: Placeable
    ) : RowItem
}
