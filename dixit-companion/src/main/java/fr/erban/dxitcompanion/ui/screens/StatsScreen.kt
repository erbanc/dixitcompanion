package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import fr.erban.dxitcompanion.db.player.PlayerViewModel
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer

@Composable
fun StatsScreen(playerViewModel: PlayerViewModel) {
    val players by playerViewModel.players.observeAsState(initial = emptyList())

    DixitScaffold(title = "Statistiques") {
        Card(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Purple)
                        .padding(vertical = 12.dp)
                ) {
                    listOf("Joueur", "Parties", "Victoires", "%").forEach { h ->
                        Text(
                            h,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                LazyColumn {
                    itemsIndexed(players) { i, player ->
                        val pct = if (player.nbGames > 0) (player.nbWins * 100 / player.nbGames) else 0
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    if (i % 2 == 0) Color.White else PurpleContainer.copy(alpha = 0.3f)
                                )
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                player.name,
                                Modifier.weight(1f),
                                color = Purple,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "${player.nbGames}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "${player.nbWins}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "$pct%",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                        HorizontalDivider(color = PurpleContainer)
                    }
                }
            }
        }
    }
}
