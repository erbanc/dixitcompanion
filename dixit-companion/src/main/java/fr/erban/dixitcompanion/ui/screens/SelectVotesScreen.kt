package fr.erban.dixitcompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.game.turn.bean.VoteBean
import fr.erban.dixitcompanion.ui.components.BottomCTA
import fr.erban.dixitcompanion.ui.components.DixitScaffold
import fr.erban.dixitcompanion.ui.components.PlayerAvatar
import fr.erban.dixitcompanion.ui.components.TurnStep
import fr.erban.dixitcompanion.ui.components.rememberHaptic
import fr.erban.dixitcompanion.ui.components.tap
import fr.erban.dixitcompanion.ui.theme.Mint
import fr.erban.dixitcompanion.ui.theme.toColorOrDefault

@Composable
fun SelectVotesScreen(
    voters: List<PlayerBean>,
    storyteller: PlayerBean,
    candidates: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (List<VoteBean>) -> Unit
) {
    val votes = remember { mutableStateMapOf<String, PlayerBean?>() }
    val haptic = rememberHaptic()
    val votedCount = voters.count { votes[it.name] != null }

    DixitScaffold(
        title = "Les votes",
        turnNumber = turnNumber,
        subtitle = "$votedCount / ${voters.size} votes enregistrés",
        step = TurnStep.Votes
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(voters) { _, voter ->
                val voterColor = voter.colorHex.toColorOrDefault()
                Card(
                    Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlayerAvatar(emoji = voter.emoji, color = voterColor, size = 36.dp)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "${voter.name} a voté pour…",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        // Storyteller card (highlighted)
                        VoteOptionRow(
                            label = storyteller.name,
                            emoji = storyteller.emoji,
                            color = Mint,
                            highlight = true,
                            selected = votes[voter.name]?.name == storyteller.name,
                            onClick = {
                                votes[voter.name] = storyteller
                                haptic.tap()
                            }
                        )
                        Spacer(Modifier.height(4.dp))
                        // Other candidates
                        candidates.filter { it.name != voter.name }.forEach { candidate ->
                            VoteOptionRow(
                                label = candidate.name,
                                emoji = candidate.emoji,
                                color = candidate.colorHex.toColorOrDefault(),
                                highlight = false,
                                selected = votes[voter.name]?.name == candidate.name,
                                onClick = {
                                    votes[voter.name] = candidate
                                    haptic.tap()
                                }
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
        val allVoted = voters.all { votes[it.name] != null }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BottomCTA("Valider les votes", enabled = allVoted) {
                val voteList = voters.mapNotNull { voter ->
                    votes[voter.name]?.let { VoteBean(voter, it) }
                }
                onContinue(voteList)
            }
        }
    }
}

@Composable
private fun VoteOptionRow(
    label: String,
    emoji: String,
    color: Color,
    highlight: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) {
        if (highlight) color.copy(alpha = 0.18f)
        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    } else Color.Transparent
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) color else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerAvatar(emoji = emoji, color = color, size = 28.dp)
        Spacer(Modifier.width(10.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (highlight) color else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (highlight) {
            Icon(
                Icons.Default.AutoAwesome,
                "carte du conteur",
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
