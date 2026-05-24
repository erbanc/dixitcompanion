package fr.erban.dxitcompanion.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.ui.components.ChartSeries
import fr.erban.dxitcompanion.ui.components.DixitLineChart
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.toColorOrDefault
import java.util.concurrent.TimeUnit

@Composable
fun ScoresResultScreen(
    game: GameBean,
    onHome: () -> Unit,
    onRematch: () -> Unit
) {
    val context = LocalContext.current
    val maxPoints = game.players.maxOfOrNull { it.currentScore } ?: 0
    val hasData = game.players.isNotEmpty() && game.players.any { it.scoresheet.isNotEmpty() }

    val series = remember(game) {
        game.players.map { player ->
            ChartSeries(
                label = "${player.emoji} ${player.name}",
                color = player.colorHex.toColorOrDefault(),
                points = player.scoresheet.map { it.turn to it.score }
            )
        }
    }

    DixitScaffold(
        title = "Évolution ✦",
        subtitle = formatDuration(game.durationMillis)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                if (!hasData) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Aucune donnée à afficher",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    DixitLineChart(
                        series = series,
                        maxTurn = game.currentTurn,
                        maxScore = maxPoints,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            // Final scoreboard
            Card(
                Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    game.players.forEachIndexed { index, p ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${index + 1}.",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "${p.emoji} ${p.name}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "${p.currentScore} pts",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            // Action row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val text = buildShareText(game)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, text)
                        }
                        context.startActivity(Intent.createChooser(intent, "Partager le résultat"))
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(Icons.Default.Share, null, Modifier.padding(end = 6.dp))
                    Text("Partager")
                }
                FilledTonalButton(
                    onClick = onRematch,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(Icons.Default.Refresh, null, Modifier.padding(end = 6.dp))
                    Text("Revanche")
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onHome,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Home, null, Modifier.padding(end = 6.dp))
                Text("Retour à l'accueil", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

private fun formatDuration(ms: Long): String {
    if (ms <= 0) return ""
    val totalMin = TimeUnit.MILLISECONDS.toMinutes(ms)
    return when {
        totalMin >= 60 -> "Durée : ${totalMin / 60} h ${totalMin % 60} min"
        totalMin >= 1 -> "Durée : $totalMin min"
        else -> "Durée : moins d'une minute"
    }
}

private fun buildShareText(game: GameBean): String {
    val winner = game.nameWinner ?: "personne"
    val players = game.players.joinToString("\n") { "• ${it.emoji} ${it.name} — ${it.currentScore} pts" }
    val duration = formatDuration(game.durationMillis).ifBlank { "—" }
    return """
        ✦ Partie de Dixit terminée ✦
        Gagnant : $winner
        $duration
        Joueurs (${game.currentTurn} tour${if (game.currentTurn > 1) "s" else ""}) :
        $players
    """.trimIndent()
}
