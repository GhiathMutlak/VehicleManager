package com.carly.vehicles.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    background = backgroundDark,
    onBackground = fontLight,
    surface = backgroundLight,
    onSurface = fontDark
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