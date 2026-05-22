package fr.erban.dxitcompanion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DixitColorScheme = lightColorScheme(
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
    surfaceVariant = PurpleContainer,
    onSurfaceVariant = Purple,
    outline = Purple,
)

@Composable
fun DixitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DixitColorScheme,
        typography = DixitTypography,
        shapes = DixitShapes,
        content = content
    )
}
