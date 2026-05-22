package fr.erban.dxitcompanion.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Gold
import fr.erban.dxitcompanion.ui.theme.Mint
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer

@Composable
fun EndTurnScreen(
    game: GameBean,
    turnNumber: Int,
    endGame: Boolean,
    winnerName: String?,
    onContinue: () -> Unit
) {
    DixitScaffold(
        title = if (endGame) "$winnerName gagne ! 🎉" else "Fin du tour"
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(game.players) { index, player ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 100L)
                    visible = true
                }
                AnimatedVisibility(
                    visible,
                    enter = slideInVertically(animationSpec = tween(300)) { it } + fadeIn(animationSpec = tween(300))
                ) {
                    val isLeader = player.name == game.players.first().name
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLeader) PurpleContainer else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(if (isLeader) 6.dp else 2.dp)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (isLeader) {
                                Text("★ ", style = MaterialTheme.typography.titleLarge, color = Gold)
                            }
                            Text(
                                player.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${player.currentScore} pts",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Purple,
                                    fontWeight = FontWeight.Bold
                                )
                                player.scoreLastTurn?.let {
                                    Text(
                                        "+$it ce tour",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Mint
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomCTA(if (endGame) "Voir le graphique →" else "Tour suivant →") { onContinue() }
    }
}
