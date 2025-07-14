package com.jws.resume.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = MedBlueDark,
    onPrimary = Color.White,
    secondary = LightTealDark,
    onSecondary = TextOnDark,
    tertiary = SoftTerracottaDark,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextOnDark,
    surface = DarkSurface,
    onSurface = TextOnDark,
    surfaceVariant = Color(0xFF2C3135),
    onSurfaceVariant = TextOnDark,
    outline = Color(0xFF8A9197)
)

private val LightColorScheme = lightColorScheme(
    primary = MedBlue,
    onPrimary = Color.White,
    secondary = LightTeal,
    onSecondary = DarkBlueGrayTextOnSecondary,
    tertiary = SoftTerracotta,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextOnLight,
    surface = LightSurface,
    onSurface = TextOnLight,
    surfaceVariant = Color(0xFFE0E3E7),
    onSurfaceVariant = TextOnLight,
    outline = Color(0xFFB0B8BF)
)

@Composable
fun ResumeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}