package com.quickclock.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF00FF00),
    primaryVariant = Color(0xFF00CC00),
    secondary = Color(0xFFFFFFFF),
    secondaryVariant = Color(0xFFCCCCCC),
    background = Color(0xFF000000),
    surface = Color(0xFF1F1F1F),
    error = Color(0xFFFF0000),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun QuickClockTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        content = content
    )
}
