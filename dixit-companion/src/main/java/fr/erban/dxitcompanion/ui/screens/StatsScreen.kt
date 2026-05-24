package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.erban.dxitcompanion.db.game.GameEntity
import fr.erban.dxitcompanion.db.game.GameViewModel
import fr.erban.dxitcompanion.db.player.PlayerEntity
import fr.erban.dxitcompanion.db.player.PlayerViewModel
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.components.PlayerAvatar
import fr.erban.dxitcompanion.ui.theme.toColorOrDefault
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatsScreen(
    playerViewModel: PlayerViewModel,
    gameViewModel: GameViewModel
) {
    var tab by remember { mutableStateOf(0) }
    val players by playerViewModel.players.observeAsState(initial = emptyList())
    val games by gameViewModel.finishedGames.observeAsState(initial = emptyList())

    DixitScaffold(title = "Statistiques") {
        Column(Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = tab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Joueurs") },
                    icon = { Icon(Icons.Default.SportsEsports, null) })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Historique") },
                    icon = { Icon(Icons.Default.History, null) })
            }
            when (tab) {
                0 -> PlayersTab(players)
                else -> HistoryTab(games)
            }
        }
    }
}

@Composable
private fun PlayersTab(players: List<PlayerEntity>) {
    if (players.isEmpty()) {
        EmptyState(
            icon = Icons.Default.SportsEsports,
            text = "Aucun joueur enregistré.\nLance une première partie pour collecter des stats !"
        )
        return
    }
    val sorted = remember(players) { players.sortedByDescending { it.nbWins } }
    val maxWins = sorted.firstOrNull()?.nbWins?.takeIf { it > 0 } ?: 1

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(sorted) { p ->
            PlayerStatCard(p, maxWins)
        }
    }
}

@Composable
private fun PlayerStatCard(p: PlayerEntity, maxWins: Int) {
    val color = p.colorHex.toColorOrDefault()
    val winRate = if (p.nbGames > 0) (p.nbWins * 100f / p.nbGames) else 0f
    Card(
        Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PlayerAvatar(emoji = p.emoji, color = color, size = 48.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        p.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${p.nbGames} partie${if (p.nbGames > 1) "s" else ""} jouée${if (p.nbGames > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        null,
                        tint = if (p.nbWins > 0) MaterialTheme.colorScheme.secondary
                              else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        "${p.nbWins}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { p.nbWins.toFloat() / maxWins.coerceAtLeast(1) },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "${winRate.toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(games: List<GameEntity>) {
    if (games.isEmpty()) {
        EmptyState(
            icon = Icons.Default.History,
            text = "Aucune partie terminée.\nJoue une partie pour voir son historique."
        )
        return
    }
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(games) { g ->
            GameHistoryCard(g)
        }
    }
}

@Composable
private fun GameHistoryCard(g: GameEntity) {
    var expanded by remember { mutableStateOf(false) }
    val dateLabel = remember(g.endedAt) {
        if (g.endedAt > 0) SimpleDateFormat("d MMM yyyy · HH:mm", Locale.getDefault()).format(Date(g.endedAt))
        else "Date inconnue"
    }
    val scoresheet: Map<String, Map<String, Int>> = remember(g.scoreSheet) {
        if (g.scoreSheet.isNullOrBlank()) emptyMap()
        else try {
            val type = object : TypeToken<Map<String, Map<String, Int>>>() {}.type
            Gson().fromJson<Map<String, Map<String, Int>>>(g.scoreSheet, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }
    // Pre-compute final score per player from the largest turn key
    val finalScores: List<Pair<String, Int>> = remember(scoresheet) {
        scoresheet.map { (name, turns) ->
            val last = turns.keys.mapNotNull { it.toIntOrNull() }.maxOrNull()?.toString()
            name to (turns[last] ?: 0)
        }.sortedByDescending { it.second }
    }

    Card(
        Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .width(46.dp)
                        .height(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "Gagnant : ${g.nameWinner ?: "?"}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "$dateLabel · ${g.nbTurns} tour${if (g.nbTurns > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    if (expanded) "▾" else "▸",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (expanded) {
                Spacer(Modifier.height(10.dp))
                finalScores.forEachIndexed { i, (name, score) ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${i + 1}.",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "$score pts",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Box(
        Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(20.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

