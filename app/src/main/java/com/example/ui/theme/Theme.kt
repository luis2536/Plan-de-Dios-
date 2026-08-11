// app/src/main/java/com/example/ui/theme/Theme.kt
package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = DeepNavy,
    primaryContainer = CardNavy,
    onPrimaryContainer = LightGold,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    background = DeepNavy,
    onBackground = TextPrimaryLight,
    surface = SurfaceNavy,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardNavy,
    onSurfaceVariant = TextSecondaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = DeepNavy,
    onPrimary = Color.White,
    primaryContainer = LightBackground,
    onPrimaryContainer = DeepNavy,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = DeepNavy,
    surface = LightSurface,
    onSurface = DeepNavy,
    surfaceVariant = LightBackground,
    onSurfaceVariant = CardNavy
)

@Composable
fun PlanDeDiosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
