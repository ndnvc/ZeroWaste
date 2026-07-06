package com.example.zerowaste.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = EarthyBrown,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = SurfaceColor,
    onSecondary = TextPrimary,
    onTertiary = SurfaceColor,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun ZeroWasteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
