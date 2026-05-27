package fr.erban.dixitcompanion.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.erban.dixitcompanion.game.GameBean
import fr.erban.dixitcompanion.ui.components.BottomCTA
import fr.erban.dixitcompanion.ui.components.ConfettiBurst
import fr.erban.dixitcompanion.ui.components.DixitScaffold
import fr.erban.dixitcompanion.ui.components.GoldenAura
import fr.erban.dixitcompanion.ui.components.PlayerAvatar
import fr.erban.dixitcompanion.ui.components.TurnStep
import fr.erban.dixitcompanion.ui.theme.Gold
import fr.erban.dixitcompanion.ui.theme.Mint
import fr.erban.dixitcompanion.ui.theme.toColorOrDefault
import kotlinx.coroutines.delay

@Composable
fun EndTurnScreen(
    game: GameBean,
    turnNumber: Int,
    endGame: Boolean,
    winnerName: String?,
    onContinue: () -> Unit
) {
    DixitScaffold(
        title = if (endGame) "$winnerName gagne ! 🎉" else "Fin du tour",
        turnNumber = if (endGame) null else turnNumber,
        step = if (endGame) null else TurnStep.EndTurn
    ) {
        if (endGame) GoldenAura(Modifier.fillMaxSize())
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(game.players) { index, player ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 90L)
                    visible = true
                }
                AnimatedVisibility(
                    visible,
                    enter = slideInVertically(animationSpec = tween(280)) { it } +
                            fadeIn(animationSpec = tween(280))
                ) {
                    ScoreRow(
                        player = player,
                        isLeader = index == 0,
                        rank = index + 1
                    )
                }
            }
        }
        if (endGame) ConfettiBurst()
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BottomCTA(if (endGame) "Voir le graphique →" else "Tour suivant →") { onContinue() }
        }
    }
}

@Composable
private fun ScoreRow(
    player: fr.erban.dixitcompanion.game.player.PlayerBean,
    isLeader: Boolean,
    rank: Int
) {
    val color = player.colorHex.toColorOrDefault()
    Card(
        Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isLeader) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isLeader) 8.dp else 2.dp)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge or trophy
            Box(
                Modifier
                    .padding(end = 10.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isLeader) Gold
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLeader) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        "leader",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(4.dp)
                    )
                } else {
                    Text(
                        "$rank",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            PlayerAvatar(emoji = player.emoji, color = color, size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Text(
                player.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${player.currentScore} pts",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                player.scoreLastTurn?.let { delta ->
                    Text(
                        when {
                            delta > 0 -> "+$delta ce tour"
                            delta < 0 -> "$delta ce tour"
                            else -> "—"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = if (delta > 0) Mint else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
