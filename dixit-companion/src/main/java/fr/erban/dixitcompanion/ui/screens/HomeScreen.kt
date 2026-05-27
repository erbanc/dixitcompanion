package fr.erban.dixitcompanion.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.erban.dixitcompanion.ui.components.FloatingOrbs
import fr.erban.dixitcompanion.ui.components.rememberHaptic
import fr.erban.dixitcompanion.ui.components.tap
import fr.erban.dixitcompanion.ui.theme.Coral
import fr.erban.dixitcompanion.ui.theme.Gold
import fr.erban.dixitcompanion.ui.theme.PurpleContainerSoft
import kotlin.random.Random

@Composable
fun HomeScreen(
    onNewGame: () -> Unit,
    onStats: () -> Unit,
    onRules: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xFF050110),
            Color(0xFF0D0524),
            Color(0xFF1E0B4A),
            Color(0xFF3B1E7A)
        )
    )
    Box(Modifier.fillMaxSize().background(gradient)) {
        StarField()
        FloatingOrbs(Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeroTitle()
            Spacer(Modifier.height(64.dp))
            DixitHomeButton(
                label = "Nouvelle partie",
                icon = Icons.Default.PlayArrow,
                containerColor = Gold,
                contentColor = Color(0xFF1E1133),
                onClick = onNewGame
            )
            Spacer(Modifier.height(14.dp))
            DixitHomeButton(
                label = "Statistiques",
                icon = Icons.Default.BarChart,
                containerColor = Color.White.copy(alpha = 0.12f),
                contentColor = Color.White,
                onClick = onStats,
                border = BorderStroke(1.dp, PurpleContainerSoft.copy(alpha = 0.5f))
            )
            Spacer(Modifier.height(14.dp))
            DixitHomeButton(
                label = "Règles du jeu",
                icon = Icons.Default.AutoStories,
                containerColor = Coral,
                contentColor = Color.White,
                onClick = onRules
            )
        }
        Text(
            "v1.1.0",
            color = Color.White.copy(alpha = 0.3f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun HeroTitle() {
    val transition = rememberInfiniteTransition(label = "hero-shine")
    val shine by transition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2400, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "shine-alpha"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Dixit",
            style = MaterialTheme.typography.displayLarge,
            color = Gold.copy(alpha = shine),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .width(28.dp)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.6f))
            )
            Text(
                "COMPANION",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                letterSpacing = 6.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                Modifier
                    .width(28.dp)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }
    }
}

@Composable
private fun DixitHomeButton(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    border: BorderStroke? = null
) {
    val haptic = rememberHaptic()
    Button(
        onClick = { haptic.tap(); onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.titleLarge)
    }
}

private data class Star(
    val x: Float, val y: Float, val r: Float,
    val baseAlpha: Float, val group: Int
)

@Composable
private fun StarField() {
    val stars = remember {
        List(90) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                r = 0.6f + Random.nextFloat() * 2.0f,
                baseAlpha = 0.25f + Random.nextFloat() * 0.75f,
                group = Random.nextInt(3)
            )
        }
    }
    val tr = rememberInfiniteTransition(label = "stars")
    val t1 by tr.animateFloat(0.18f, 0.95f, infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse), "s1")
    val t2 by tr.animateFloat(0.30f, 1.00f, infiniteRepeatable(tween(3100, easing = LinearEasing), RepeatMode.Reverse), "s2")
    val t3 by tr.animateFloat(0.10f, 0.80f, infiniteRepeatable(tween(1700, easing = LinearEasing), RepeatMode.Reverse), "s3")

    Canvas(Modifier.fillMaxSize()) {
        stars.forEach { star ->
            val t = when (star.group) { 0 -> t1; 1 -> t2; else -> t3 }
            drawCircle(
                Color(0xFFFFFCE0),
                radius = star.r,
                center = Offset(star.x * size.width, star.y * size.height),
                alpha = star.baseAlpha * t
            )
        }
    }
}
