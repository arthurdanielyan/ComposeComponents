package com.danielyan.coreCompose.components.switchC

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


@Immutable
data class SwitchColors(
    val checkedBackground: Color,
    val uncheckedBackground: Color,
    val thumb: Color,
) {

    companion object {

        @Composable
        fun default(
            checkedBackground: Color = MaterialTheme.colorScheme.primary,
            uncheckedBackground: Color = MaterialTheme.colorScheme.tertiary,
            thumb: Color = MaterialTheme.colorScheme.onPrimary,
        ): SwitchColors {
            return SwitchColors(
                checkedBackground = checkedBackground,
                uncheckedBackground = uncheckedBackground,
                thumb = thumb,
            )
        }
    }
}
