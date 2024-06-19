package com.example.catapult.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkPrimaryDark,
    tertiary = DarkAccent,

    background = Color(0xFF2E2B2B),
    surface = Color(0xFF1E1E1E),
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightPrimaryDark,
    tertiary = LightAccent,

    background = Color(0xFFDACECE),
    surface = Color(0xFFDBD5DA),
)

@Composable
fun CatapultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Removed the view part for enableEdgeToEdge() function

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}