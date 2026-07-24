package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonColorScheme = darkColorScheme(
    primary = NeonPrimaryVariant,
    onPrimary = NeonOnPrimary,
    primaryContainer = NeonPrimary,
    onPrimaryContainer = NeonOnPrimary,
    secondary = NeonSecondary,
    onSecondary = NeonOnSecondary,
    secondaryContainer = NeonSecondaryContainer,
    tertiary = NeonTertiary,
    tertiaryContainer = NeonTertiaryContainer,
    background = NeonBackground,
    onBackground = NeonOnSurface,
    surface = NeonSurface,
    onSurface = NeonOnSurface,
    surfaceVariant = NeonSurfaceHighest,
    onSurfaceVariant = NeonOutline,
    outline = NeonOutline
)

@Composable
fun NeonLoungeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography = Typography,
        content = content
    )
}

