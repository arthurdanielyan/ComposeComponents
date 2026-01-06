package com.danielyan.coreCompose.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A circular loading indicator with animated rotation, expanding arcs,
 * and cycling colors.
 *
 * Features:
 * - The arc smoothly expands and contracts to give a “breathing” effect.
 * - The entire indicator rotates continuously.
 * - Colors cycle through the provided list for a dynamic multicolor effect.
 *
 * @param modifier Modifier applied to the loading indicator.
 * @param colors List of colors used for the indicator. The colors are cycled
 * continuously. Defaults to a set of Google-style colors:
 * Yellow (`0xFFF4B400`), Green (`0xFF0F9D58`), Red (`0xFFDB4437`), Blue (`0xFF4285F4`).
 * @param strokeWidth Width of the progress arc. Defaults to `4.dp`.
 */
@Composable
fun CircularLoading(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFFF4B400),
        Color(0xFF0F9D58),
        Color(0xFFDB4437),
        Color(0xFF4285F4)
    ),
    strokeWidth: Dp = 4.dp
) {
    val expansionDuration by remember { mutableStateOf(700) }
    val infiniteTransition = rememberInfiniteTransition()


    val currentColorIndex by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = colors.size,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 2*expansionDuration*colors.size,
                easing = LinearEasing
            )
        )
    )

    val progress by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = expansionDuration,
                easing = LinearEasing
            )
        )
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = expansionDuration,
                easing = LinearEasing
            )
        )
    )

    CircularProgressIndicator(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
            },
        trackColor = Color.Transparent,
        progress = { progress },
        color = colors[currentColorIndex],
        strokeWidth = strokeWidth
    )
}
