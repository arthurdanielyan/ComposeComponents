package com.danielyan.coreCompose.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * A circular dots loading indicator with animated scaling dots.
 *
 * Features:
 * - Dots are arranged in a circle and scale up and down in a smooth
 *   continuous cycle to create a rotating effect.
 * - The animation repeats infinitely.
 * - The number of dots, animation speed, and minimum dot scale can be customized.
 *
 * @param modifier Modifier applied to the loader.
 * @param color Color of the dots.
 * @param dotsCount Number of dots arranged in the circle. Defaults to `8`.
 * @param animationDurationMillis Duration of one full animation cycle in milliseconds.
 * Defaults to `1200`.
 * @param minDotScale Minimum scale factor for each dot during the animation.
 * Defaults to `0.45f`.
 */
@Composable
fun CircularDotsLoader(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    dotsCount: Int = 8,
    animationDurationMillis: Int = 1200,
    minDotScale: Float = 0.45f,
) {
    val transition = rememberInfiniteTransition(label = "dotsLoader")

    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sharedProgress"
    )

    Canvas(
        modifier = modifier
            .size(40.dp)
            .semantics { contentDescription = "Loading" }
    ) {
        val canvasRadius = min(size.width, size.height) / 2f
        val dotRadius = canvasRadius * 0.22f
        val ringRadius = canvasRadius - dotRadius
        val angleStep = (2 * PI / dotsCount).toFloat()

        val p = progress.value

        for (index in 0 until dotsCount) {
            val angle = angleStep * index

            val offset = (index.toFloat() / dotsCount)
            val localProgress = (p - offset).mod(1f)

            // Smooth grow → shrink cycle
            val scale = minDotScale + (1f - minDotScale) *
                    (1f - abs(localProgress * 2f - 1f))

            val center = Offset(
                x = center.x + cos(angle) * ringRadius,
                y = center.y + sin(angle) * ringRadius
            )

            drawCircle(
                color = color,
                radius = dotRadius * scale,
                center = center
            )
        }
    }
}
