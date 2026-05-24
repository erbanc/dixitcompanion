package fr.erban.dxitcompanion.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.Gold
import fr.erban.dxitcompanion.ui.theme.Mint
import fr.erban.dxitcompanion.ui.theme.Purple
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val color: Color,
    val angleRad: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val width: Float,
    val height: Float
)

@Composable
fun ConfettiBurst(
    modifier: Modifier = Modifier,
    particleCount: Int = 120,
    durationMillis: Int = 2200
) {
    val palette = listOf(Gold, Coral, Mint, Purple, Color(0xFFE74C8C), Color(0xFFFFC857))
    val particles = remember {
        List(particleCount) {
            Particle(
                color = palette.random(),
                angleRad = (Random.nextFloat() * 2f * PI.toFloat()),
                speed = 0.6f + Random.nextFloat() * 1.4f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f,
                width = 6f + Random.nextFloat() * 6f,
                height = 10f + Random.nextFloat() * 10f
            )
        }
    }
    var launched by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { launched = true }
    val progress by animateFloatAsState(
        targetValue = if (launched) 1f else 0f,
        animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing),
        label = "confetti-anim"
    )

    Canvas(modifier.fillMaxSize()) {
        if (progress <= 0f) return@Canvas
        val origin = Offset(size.width / 2f, size.height * 0.35f)
        particles.forEach { p ->
            // Position projectile motion
            val t = progress
            val dx = cos(p.angleRad) * p.speed * size.width * 0.5f * t
            val dy = sin(p.angleRad) * p.speed * size.height * 0.4f * t +
                     0.5f * (size.height * 1.2f) * t * t // gravity
            val cx = origin.x + dx
            val cy = origin.y + dy
            // Fade out near the end
            val alpha = (1f - (t * t)).coerceIn(0f, 1f)
            rotate(degrees = p.rotation + p.rotationSpeed * t, pivot = Offset(cx, cy)) {
                drawRect(
                    color = p.color.copy(alpha = alpha),
                    topLeft = Offset(cx - p.width / 2f, cy - p.height / 2f),
                    size = Size(p.width, p.height)
                )
            }
        }
    }
}
