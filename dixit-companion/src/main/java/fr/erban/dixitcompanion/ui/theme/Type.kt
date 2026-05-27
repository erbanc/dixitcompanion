package fr.erban.dixitcompanion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.erban.dixitcompanion.R

val WriteMeASong = FontFamily(Font(R.font.write_me_a_song))

val DixitTypography = Typography(
    displayLarge  = TextStyle(fontFamily = WriteMeASong, fontSize = 72.sp, lineHeight = 76.sp),
    displayMedium = TextStyle(fontFamily = WriteMeASong, fontSize = 52.sp, lineHeight = 56.sp),
    headlineLarge = TextStyle(fontFamily = WriteMeASong, fontSize = 40.sp, lineHeight = 44.sp),
    headlineMedium= TextStyle(fontFamily = WriteMeASong, fontSize = 32.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = WriteMeASong, fontSize = 26.sp, lineHeight = 30.sp),
    titleLarge    = TextStyle(fontFamily = WriteMeASong, fontSize = 22.sp, lineHeight = 26.sp),
    titleMedium   = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyLarge     = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp),
    bodyMedium    = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp),
    labelLarge    = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 14.sp),
    labelMedium   = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 12.sp),
)
