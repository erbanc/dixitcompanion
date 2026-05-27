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
import androidx.compose.ui.graphics.drawscope.DrawScope
import fr.erban.dixitcompanion.ui.theme.Gold
import fr.erban.dixitcompanion.ui.theme.NebulaCeleste
import fr.erban.dixitcompanion.ui.theme.NebulaCobalt
import fr.erban.dixitcompanion.ui.theme.NebulaMauve
import fr.erban.dixitcompanion.ui.theme.NebulaRose

private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

private fun DrawScope.orb(color: Color, alpha: Float, cx: Float, cy: Float, radius: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color.copy(alpha = alpha), Color.Transparent),
            center = Offset(cx, cy),
            radius = radius
        ),
        radius = radius,
        center = Offset(cx, cy)
    )
}

/**
 * Fond onirique animé pour tous les écrans intérieurs (dark mode).
 * 5 orbes nébuleuses driftant lentement, couvrant toute la hauteur.
 */
@Composable
fun DreamBackground(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "dream")
    val t1 by tr.animateFloat(0f, 1f, infiniteRepeatable(tween(9000,  easing = LinearEasing), RepeatMode.Reverse), "d1")
    val t2 by tr.animateFloat(0f, 1f, infiniteRepeatable(tween(7400,  easing = LinearEasing), RepeatMode.Reverse), "d2")
    val t3 by tr.animateFloat(0f, 1f, infiniteRepeatable(tween(11200, easing = LinearEasing), RepeatMode.Reverse), "d3")
    val t4 by tr.animateFloat(0f, 1f, infiniteRepeatable(tween(6600,  easing = LinearEasing), RepeatMode.Reverse), "d4")
    val t5 by tr.animateFloat(0f, 1f, infiniteRepeatable(tween(8500,  easing = LinearEasing), RepeatMode.Reverse), "d5")

    Canvas(modifier) {
        val w = size.width; val h = size.height

        // Mauve — haut gauche, dérive vers la droite
        orb(NebulaMauve, lerp(0.18f, 0.34f, t1),
            lerp(w * 0.10f, w * 0.48f, t2), lerp(h * 0.05f, h * 0.20f, t3), w * 0.62f)
        // Or — droite milieu
        orb(Gold, lerp(0.08f, 0.18f, t3),
            lerp(w * 0.72f, w * 0.98f, t1), lerp(h * 0.28f, h * 0.48f, t4), w * 0.44f)
        // Rose — bord gauche bas
        orb(NebulaRose, lerp(0.07f, 0.16f, t4),
            lerp(0f, w * 0.20f, t5), lerp(h * 0.52f, h * 0.74f, t2), w * 0.40f)
        // Teal — bas centre, remonte
        orb(NebulaCeleste, lerp(0.05f, 0.14f, t2),
            lerp(w * 0.32f, w * 0.62f, t3), lerp(h * 0.74f, h * 0.95f, t1), w * 0.52f)
        // Cobalt — centre, nappage diffus
        orb(NebulaCobalt, lerp(0.04f, 0.10f, t5),
            w * 0.50f, lerp(h * 0.38f, h * 0.58f, t4), w * 0.78f)
    }
}

/** 5 orbes pour l'écran d'accueil — plus dramatiques que DreamBackground. */
@Composable
fun FloatingOrbs(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "orbs")
    val a1 by tr.animateFloat(0.22f, 0.48f, infiniteRepeatable(tween(3600, easing = LinearEasing), RepeatMode.Reverse), "o1")
    val a2 by tr.animateFloat(0.24f, 0.52f, infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse), "o2")
    val a3 by tr.animateFloat(0.12f, 0.32f, infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Reverse), "o3")
    val a4 by tr.animateFloat(0.08f, 0.22f, infiniteRepeatable(tween(4100, easing = LinearEasing), RepeatMode.Reverse), "o4")
    val a5 by tr.animateFloat(0.06f, 0.18f, infiniteRepeatable(tween(2900, easing = LinearEasing), RepeatMode.Reverse), "o5")

    Canvas(modifier) {
        val w = size.width; val h = size.height
        orb(Gold,          a1, w * 0.50f, h * 0.26f, w * 0.52f)
        orb(NebulaMauve,   a2, w * 0.07f, h * 0.34f, w * 0.42f)
        orb(NebulaRose,    a3, w * 0.93f, h * 0.18f, w * 0.34f)
        orb(NebulaCeleste, a4, w * 0.14f, h * 0.74f, w * 0.36f)
        orb(NebulaCobalt,  a5, w * 0.86f, h * 0.68f, w * 0.32f)
    }
}

/** Halo doré pulsant depuis le haut — victoire / fin de partie. */
@Composable
fun GoldenAura(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "aura")
    val a1 by tr.animateFloat(0.14f, 0.40f, infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse), "ga1")
    val a2 by tr.animateFloat(0.06f, 0.20f, infiniteRepeatable(tween(3600, easing = LinearEasing), RepeatMode.Reverse), "ga2")
    val a3 by tr.animateFloat(0.04f, 0.14f, infiniteRepeatable(tween(4400, easing = LinearEasing), RepeatMode.Reverse), "ga3")

    Canvas(modifier) {
        val w = size.width; val h = size.height
        orb(Gold,          a1, w * 0.50f, 0f,        w * 0.95f)
        orb(NebulaMauve,   a2, w * 0.20f, h * 0.15f, w * 0.55f)
        orb(NebulaCeleste, a3, w * 0.80f, h * 0.10f, w * 0.45f)
    }
}
