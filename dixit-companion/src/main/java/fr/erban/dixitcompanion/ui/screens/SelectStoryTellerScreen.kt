package fr.erban.dixitcompanion.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.ui.components.BottomCTA
import fr.erban.dixitcompanion.ui.components.DixitScaffold
import fr.erban.dixitcompanion.ui.components.PlayerAvatar
import fr.erban.dixitcompanion.ui.components.TurnStep
import fr.erban.dixitcompanion.ui.theme.toColorOrDefault

@Composable
fun SelectStoryTellerScreen(
    players: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (PlayerBean) -> Unit
) {
    var selected by remember { mutableStateOf<PlayerBean?>(null) }

    DixitScaffold(
        title = "Qui raconte ?",
        turnNumber = turnNumber,
        step = TurnStep.Storyteller
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(players) { player ->
                StoryTellerCard(
                    player = player,
                    isSelected = selected?.name == player.name,
                    onClick = { selected = player }
                )
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BottomCTA("Confirmer", enabled = selected != null) {
                selected?.let { onContinue(it) }
            }
        }
    }
}

@Composable
private fun StoryTellerCard(
    player: PlayerBean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = player.colorHex.toColorOrDefault()
    val tr = rememberInfiniteTransition(label = "glow")
    val glowAlpha by tr.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.62f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "glow-a"
    )
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (isSelected) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(color.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(size.width * 0.5f, size.height * 0.5f),
                            radius = size.width * 0.72f
                        ),
                        radius = size.width * 0.72f,
                        center = Offset(size.width * 0.5f, size.height * 0.5f)
                    )
                }
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(Modifier.width(4.dp))
            PlayerAvatar(emoji = player.emoji, color = color, size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    player.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${player.currentScore} pts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
