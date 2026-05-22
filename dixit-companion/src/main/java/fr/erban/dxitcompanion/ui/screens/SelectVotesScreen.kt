package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Mint
import fr.erban.dxitcompanion.ui.theme.Purple

@Composable
fun SelectVotesScreen(
    voters: List<PlayerBean>,
    storyteller: PlayerBean,
    candidates: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (List<VoteBean>) -> Unit
) {
    val votes = remember { mutableStateMapOf<String, PlayerBean?>() }

    DixitScaffold(title = "Les votes", turnNumber = turnNumber) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(voters) { _, voter ->
                Card(
                    Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "${voter.name} a voté pour :",
                            style = MaterialTheme.typography.titleMedium,
                            color = Purple
                        )
                        Spacer(Modifier.height(8.dp))
                        // Candidates (other non-storytellers)
                        candidates.filter { it.name != voter.name }.forEach { candidate ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                RadioButton(
                                    selected = votes[voter.name]?.name == candidate.name,
                                    onClick = { votes[voter.name] = candidate },
                                    colors = RadioButtonDefaults.colors(selectedColor = Purple)
                                )
                                Text(candidate.name, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        // Option: storyteller's card
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = votes[voter.name]?.name == storyteller.name,
                                onClick = { votes[voter.name] = storyteller },
                                colors = RadioButtonDefaults.colors(selectedColor = Mint)
                            )
                            Text(
                                "Carte de ${storyteller.name} ✦",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Mint
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        val allVoted = voters.all { votes[it.name] != null }
        BottomCTA("Valider les votes", enabled = allVoted) {
            val voteList = voters.mapNotNull { voter ->
                votes[voter.name]?.let { VoteBean(voter, it) }
            }
            onContinue(voteList)
        }
    }
}
