package com.carly.vehicles.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    background = backgroundDark,
    onBackground = primaryText,
    surface = backgroundLight,
    onSurface = secondaryText,
    onPrimaryContainer = fontLight,
    onSecondaryContainer = fontDark,
    tertiaryContainer = backgroundItem
)

@Composable
fun VehicleManagerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}