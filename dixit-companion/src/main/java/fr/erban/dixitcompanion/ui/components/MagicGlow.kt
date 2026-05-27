package fr.erban.dixitcompanion.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import fr.erban.dixitcompanion.ui.theme.Gold

/** Animated gold + purple + rose orbs — suited for dark backgrounds (HomeScreen). */
@Composable
fun FloatingOrbs(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "orbs")
    val a1 by tr.animateFloat(
        initialValue = 0.22f, targetValue = 0.52f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse),
        label = "a1"
    )
    val a2 by tr.animateFloat(
        initialValue = 0.28f, targetValue = 0.56f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse),
        label = "a2"
    )
    val a3 by tr.animateFloat(
        initialValue = 0.12f, targetValue = 0.36f,
        animationSpec = infiniteRepeatable(tween(2700, easing = LinearEasing), RepeatMode.Reverse),
        label = "a3"
    )
    Canvas(modifier) {
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Gold.copy(alpha = a1), Color.Transparent),
                center = Offset(size.width * 0.50f, size.height * 0.30f),
                radius = size.width * 0.46f
            ),
            radius = size.width * 0.46f,
            center = Offset(size.width * 0.50f, size.height * 0.30f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFB47EFF).copy(alpha = a2), Color.Transparent),
                center = Offset(size.width * 0.10f, size.height * 0.38f),
                radius = size.width * 0.34f
            ),
            radius = size.width * 0.34f,
            center = Offset(size.width * 0.10f, size.height * 0.38f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFF8FAB).copy(alpha = a3), Color.Transparent),
                center = Offset(size.width * 0.90f, size.height * 0.22f),
                radius = size.width * 0.30f
            ),
            radius = size.width * 0.30f,
            center = Offset(size.width * 0.90f, size.height * 0.22f)
        )
    }
}

/** Pulsating gold radial glow from top — suited for victory / end-game moments. */
@Composable
fun GoldenAura(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "aura")
    val alpha by tr.animateFloat(
        initialValue = 0.12f, targetValue = 0.32f,
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Reverse),
        label = "aura-a"
    )
    Canvas(modifier) {
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Gold.copy(alpha = alpha), Color.Transparent),
                center = Offset(size.width * 0.50f, size.height * 0.05f),
                radius = size.width * 0.85f
            ),
            radius = size.width * 0.85f,
            center = Offset(size.width * 0.50f, size.height * 0.05f)
        )
    }
}
