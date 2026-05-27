package fr.erban.dixitcompanion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DixitLightColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = DeepNight,
    secondary = Gold,
    onSecondary = DeepNight,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = DeepNight,
    tertiary = Coral,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainer,
    onTertiaryContainer = DeepNight,
    background = Ivory,
    onBackground = DeepNight,
    surface = SurfaceWhite,
    onSurface = DeepNight,
    surfaceVariant = PurpleContainerSoft,
    onSurfaceVariant = Purple,
    outline = PurpleLight,
    outlineVariant = PurpleContainer,
    error = Coral,
    onError = Color.White
)

private val DixitDarkColorScheme = darkColorScheme(
    primary = PurpleNight,
    onPrimary = DeepNight,
    primaryContainer = PurpleNightContainer,
    onPrimaryContainer = Color.White,
    secondary = GoldNight,
    onSecondary = DeepNight,
    secondaryContainer = Color(0xFF5C3F00),
    onSecondaryContainer = GoldNight,
    tertiary = CoralNight,
    onTertiary = DeepNight,
    tertiaryContainer = Color(0xFF5C2F22),
    onTertiaryContainer = CoralNight,
    background = BackgroundNight,
    onBackground = OnSurfaceNight,
    surface = SurfaceNight,
    onSurface = OnSurfaceNight,
    surfaceVariant = SurfaceNightVariant,
    onSurfaceVariant = OnSurfaceNightVariant,
    outline = PurpleNight,
    outlineVariant = PurpleNightContainer,
    error = CoralNight,
    onError = DeepNight
)

@Composable
fun DixitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DixitDarkColorScheme else DixitLightColorScheme,
        typography = DixitTypography,
        shapes = DixitShapes,
        content = content
    )
}
