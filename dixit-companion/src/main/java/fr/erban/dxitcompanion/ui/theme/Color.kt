package fr.erban.dxitcompanion.ui.theme

import androidx.compose.ui.graphics.Color

// === LIGHT PALETTE ===
val Purple = Color(0xFF5C3A9E)
val PurpleDark = Color(0xFF3A2275)
val PurpleLight = Color(0xFF8B6FCE)
val PurpleContainer = Color(0xFFD9C7F5)        // darkened from 0xFFEDE0FF for better contrast
val PurpleContainerSoft = Color(0xFFEDE0FF)    // softer variant kept for backgrounds

val Gold = Color(0xFFECA52E)
val GoldDark = Color(0xFFC4861F)
val GoldContainer = Color(0xFFFFE7B3)

val Coral = Color(0xFFE5735D)
val CoralDark = Color(0xFFB8543F)
val CoralContainer = Color(0xFFFFD4CC)

val Mint = Color(0xFF5DBFB8)
val MintDark = Color(0xFF3B8A85)
val MintContainer = Color(0xFFC4ECE9)

val Ivory = Color(0xFFFAF6EE)                  // slightly more neutral than 0xFFFEF6EB
val DeepNight = Color(0xFF1E1133)
val SurfaceWhite = Color(0xFFFFFFFF)

// === DARK PALETTE ===
val PurpleNight = Color(0xFFB39DDB)             // light purple for dark mode primary
val PurpleNightContainer = Color(0xFF3A2275)
val GoldNight = Color(0xFFFFD580)
val CoralNight = Color(0xFFFFA48F)
val MintNight = Color(0xFF8FD8D3)
val BackgroundNight = Color(0xFF14091F)
val SurfaceNight = Color(0xFF221538)
val SurfaceNightVariant = Color(0xFF2D1F47)
val OnSurfaceNight = Color(0xFFF0E6FF)
val OnSurfaceNightVariant = Color(0xFFB5A4D4)

// === PLAYER ASSIGNABLE COLORS ===
// Curated palette for per-player identity. 12 distinct hues with good contrast.
val PlayerColors: List<Color> = listOf(
    Color(0xFF5C3A9E), // Royal Purple
    Color(0xFFE5735D), // Coral
    Color(0xFF5DBFB8), // Mint
    Color(0xFFECA52E), // Gold
    Color(0xFFE74C8C), // Pink
    Color(0xFF4A90D9), // Sky Blue
    Color(0xFF52A65A), // Forest
    Color(0xFFD4523A), // Brick
    Color(0xFF8E5BC4), // Lavender
    Color(0xFFCF8B3E), // Bronze
    Color(0xFF3CA4A0), // Teal
    Color(0xFFB85DAA)  // Magenta
)

// === PLAYER ASSIGNABLE EMOJIS ===
val PlayerEmojis: List<String> = listOf(
    "🎭", "🦊", "🐉", "🦉", "🐲", "🧙", "🎨", "🃏",
    "🌙", "⭐", "🔮", "🦄", "🐺", "🪄", "🌟", "🎪"
)
