package com.danielyan.coreCompose.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * A single-line text composable that scrolls horizontally (marquee) when the text
 * exceeds the available width.
 *
 * Features:
 * - Automatically detects if the text overflows the container width and enables
 *   horizontal scrolling.
 * - Fades out edges using [opacityWidth] for a smooth visual effect while scrolling.
 * - Supports standard [Text] styling parameters like color, fontSize, fontWeight, etc.
 * - The [onTextLayout] callback is called after the text layout, useful for measuring or other logic.
 *
 * @param text The text to display.
 * @param modifier Modifier applied to the text container.
 * @param opacityWidth Width of the fading gradient at the left and right edges of the text
 * when scrolling. Defaults to `16.dp`.
 * @param color Text color. Defaults to [Color.Unspecified], which inherits from the style.
 * @param fontSize Font size of the text. Defaults to [TextUnit.Unspecified], which inherits from the style.
 * @param fontStyle Optional font style (e.g., Italic). Defaults to `null`.
 * @param fontWeight Optional font weight (e.g., Bold). Defaults to `null`.
 * @param fontFamily Optional font family. Defaults to `null`.
 * @param letterSpacing Letter spacing for the text. Defaults to [TextUnit.Unspecified].
 * @param textDecoration Optional text decoration (e.g., underline). Defaults to `null`.
 * @param textAlign Horizontal alignment of the text. Defaults to [TextAlign.Unspecified].
 * @param lineHeight Line height of the text. Defaults to [TextUnit.Unspecified].
 * @param style Text style to merge with other parameters. Defaults to [LocalTextStyle.current].
 * @param onTextLayout Callback invoked with the [TextLayoutResult] after the text is laid out.
 * Defaults to an empty lambda.
 */
@Composable
fun RunningText(
    text: String,
    modifier: Modifier = Modifier,
    opacityWidth: Dp = 16.dp,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val measurer = rememberTextMeasurer()

    SubcomposeLayout(modifier) { constraints ->
        // Measure the text width independently (single line, no wrap)
        val layoutResult = measurer.measure(
            text = text,
            style = style.merge(
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight
            ),
            maxLines = 1,
            softWrap = false
        )
        val textPxWidth = layoutResult.size.width
        val containerWidth = constraints.maxWidth
        val shouldScroll = textPxWidth > containerWidth

        // Subcompose exactly one version with the *final* modifiers
        val placeables = subcompose(if (shouldScroll) text else "static") {
            val base = Modifier
                .then (
                    if(shouldScroll) {
                        Modifier.transparentEdges(opacityWidth = opacityWidth)
                            .marquee()
                    } else Modifier
            )

            Text(
                text = text,
                modifier = base,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                maxLines = 1,
                softWrap = false,
                onTextLayout = onTextLayout,
                style = style,
            )
        }.map { measurable ->
            // Force width to container so we don't relayout later
            measurable.measure(
                Constraints.fixedWidth(containerWidth).copy(
                    minHeight = 0,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val height = placeables.maxOf { it.height }.coerceAtLeast(0)
        layout(containerWidth, height) {
            placeables.forEach { it.place(0, 0) }
        }
    }
}

private fun Modifier.marquee() = this.basicMarquee(
    iterations = Int.MAX_VALUE,
)

private fun Modifier.transparentEdges(
    opacityWidth: Dp = 16.dp,
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()

        if (this.size.width < 2 * opacityWidth.toPx()) return@drawWithContent

        val opacityWidthPx = opacityWidth.toPx()

        drawRect(
            topLeft = Offset.Zero,
            size = Size(
                width = opacityWidthPx,
                height = size.height
            ),
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                ),
                startX = 0f,
                endX = opacityWidthPx
            ),
            blendMode = BlendMode.DstIn
        )

        drawRect(
            topLeft = Offset(
                x = size.width - opacityWidthPx,
                y = 0f
            ),
            size = Size(
                width = opacityWidthPx,
                height = size.height
            ),
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Black,
                    Color.Transparent
                ),
                startX = size.width - opacityWidthPx,
                endX = size.width
            ),
            blendMode = BlendMode.DstIn
        )
    }
    .clipToBounds()
