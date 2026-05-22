package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.DeepNight
import fr.erban.dxitcompanion.ui.theme.Gold
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer
import kotlin.random.Random

@Composable
fun HomeScreen(
    onNewGame: () -> Unit,
    onStats: () -> Unit,
    onRules: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        listOf(Color(0xFF2A1660), Color(0xFF5C3A9E), Color(0xFF7152BA))
    )
    Box(Modifier.fillMaxSize().background(gradient)) {
        StarField()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Dixit",
                style = MaterialTheme.typography.displayLarge,
                color = Gold,
                textAlign = TextAlign.Center
            )
            Text(
                "Companion",
                style = MaterialTheme.typography.titleMedium,
                color = PurpleContainer,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            DixitButton("❆  Nouvelle partie", Gold, DeepNight, onNewGame)
            Spacer(Modifier.height(16.dp))
            DixitButton(
                "♟  Statistiques",
                Purple.copy(alpha = 0.7f),
                Color.White,
                onStats,
                border = BorderStroke(1.dp, PurpleContainer)
            )
            Spacer(Modifier.height(16.dp))
            DixitButton("📖  Règles du jeu", Coral, Color.White, onRules)
        }
    }
}

@Composable
private fun DixitButton(
    label: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    border: BorderStroke? = null
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun StarField() {
    val stars = remember { List(40) { Offset(Random.nextFloat(), Random.nextFloat()) } }
    Canvas(Modifier.fillMaxSize()) {
        stars.forEach { (fx, fy) ->
            val r = (1f + fx * 3f)
            drawCircle(
                Color(0xFFFFFFE0),
                radius = r,
                center = Offset(fx * size.width, fy * size.height * 0.6f),
                alpha = 0.5f + fx * 0.4f
            )
        }
    }
}
