package fr.erban.dixitcompanion.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun rememberHaptic(): HapticFeedback = LocalHapticFeedback.current

inline fun HapticFeedback.tap() {
    performHapticFeedback(HapticFeedbackType.TextHandleMove)
}

inline fun HapticFeedback.confirm() {
    performHapticFeedback(HapticFeedbackType.LongPress)
}
