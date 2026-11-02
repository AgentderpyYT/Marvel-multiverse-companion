package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ComicColorScheme = darkColorScheme(
  primary = MarvelRed,
  onPrimary = ComicTextPrimary,
  primaryContainer = MarvelDarkRed,
  onPrimaryContainer = MarvelGold,
  secondary = MarvelGold,
  onSecondary = ComicDarkBg,
  secondaryContainer = ComicCardElevated,
  onSecondaryContainer = MarvelGold,
  tertiary = MarvelBlue,
  onTertiary = ComicDarkBg,
  background = ComicDarkBg,
  onBackground = ComicTextPrimary,
  surface = ComicSurface,
  onSurface = ComicTextPrimary,
  surfaceVariant = ComicCard,
  onSurfaceVariant = ComicTextSecondary,
  outline = ComicBorder,
  error = HealthRed,
  onError = ComicTextPrimary
)

@Composable
fun MultiverseTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = ComicColorScheme,
    typography = ComicTypography,
    content = content
  )
}
