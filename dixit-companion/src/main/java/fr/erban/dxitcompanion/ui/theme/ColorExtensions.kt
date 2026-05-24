package fr.erban.dxitcompanion.ui.theme

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

fun Color.toHexString(): String {
    val r = (red * 255).roundToInt()
    val g = (green * 255).roundToInt()
    val b = (blue * 255).roundToInt()
    return String.format("#%02X%02X%02X", r, g, b)
}

fun String.toColorOrDefault(default: Color = Purple): Color = try {
    val cleaned = if (startsWith("#")) substring(1) else this
    val long = cleaned.toLong(16)
    when (cleaned.length) {
        6 -> Color(0xFF000000 or long)
        8 -> Color(long)
        else -> default
    }
} catch (e: NumberFormatException) {
    default
}
