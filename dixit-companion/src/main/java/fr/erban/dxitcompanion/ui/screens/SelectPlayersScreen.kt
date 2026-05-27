package fr.erban.dxitcompanion.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.components.EmojiColorPickerDialog
import fr.erban.dxitcompanion.ui.components.rememberHaptic
import fr.erban.dxitcompanion.ui.components.tap
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.PlayerColors
import fr.erban.dxitcompanion.ui.theme.PlayerEmojis
import fr.erban.dxitcompanion.ui.theme.toColorOrDefault
import fr.erban.dxitcompanion.ui.theme.toHexString

data class PlayerDraft(val name: String, val emoji: String, val colorHex: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectPlayersScreen(
    initialPlayers: List<PlayerDraft> = emptyList(),
    knownPlayers: List<PlayerDraft> = emptyList(),
    onContinue: (List<PlayerDraft>) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var players by remember { mutableStateOf(initialPlayers) }
    var editing by remember { mutableStateOf<Int?>(null) }
    val haptic = rememberHaptic()

    val suggestions by remember(input, knownPlayers, players) {
        derivedStateOf {
            if (input.isBlank()) emptyList()
            else knownPlayers.filter { k ->
                k.name.contains(input.trim(), ignoreCase = true) &&
                players.none { p -> p.name.equals(k.name, ignoreCase = true) }
            }
        }
    }

    fun addPlayer(draft: PlayerDraft) {
        if (draft.name.isBlank()) return
        if (players.none { it.name.equals(draft.name, ignoreCase = true) }) {
            players = players + draft
            input = ""
            haptic.tap()
        }
    }

    DixitScaffold(title = "Joueurs") {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        label = { Text("Nom du joueur") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = { addPlayer(nextDraft(input.trim(), players)) }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = { addPlayer(nextDraft(input.trim(), players)) },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Add, "Ajouter", tint = Color.White)
                    }
                }
            }
            // Suggestions inline — pas de popup, pas de conflit avec le bouton Ajouter
            AnimatedVisibility(suggestions.isNotEmpty()) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        suggestions.take(5).forEachIndexed { index, suggestion ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { addPlayer(suggestion) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(suggestion.emoji, style = TextStyle(fontSize = 20.sp))
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    suggestion.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    Icons.Default.Add,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (index < minOf(suggestions.size, 5) - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Touche un joueur pour personnaliser son avatar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(players.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    players.forEachIndexed { index, p ->
                        PlayerChipEditable(
                            player = p,
                            onEdit = { editing = index },
                            onRemove = { players = players.filterIndexed { i, _ -> i != index } }
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            if (players.size < 3) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✦", color = Coral, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Minimum 3 joueurs (${3 - players.size} restant${if (3 - players.size > 1) "s" else ""})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Coral
                    )
                }
            }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BottomCTA("Continuer", enabled = players.size >= 3) { onContinue(players) }
        }
        editing?.let { idx ->
            val current = players[idx]
            EmojiColorPickerDialog(
                initialEmoji = current.emoji,
                initialColorHex = current.colorHex,
                title = current.name,
                onDismiss = { editing = null },
                onConfirm = { e, c ->
                    players = players.toMutableList().also {
                        it[idx] = current.copy(emoji = e, colorHex = c)
                    }
                    editing = null
                }
            )
        }
    }
}

private fun nextDraft(name: String, existing: List<PlayerDraft>): PlayerDraft {
    val usedColors = existing.map { it.colorHex.uppercase() }.toSet()
    val color = PlayerColors.firstOrNull { it.toHexString().uppercase() !in usedColors }
        ?: PlayerColors[existing.size % PlayerColors.size]
    val emoji = PlayerEmojis[existing.size % PlayerEmojis.size]
    return PlayerDraft(name = name, emoji = emoji, colorHex = color.toHexString())
}

@Composable
private fun PlayerChipEditable(
    player: PlayerDraft,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    val color = player.colorHex.toColorOrDefault()
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color)
            .clickable { onEdit() }
            .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text(player.emoji, style = TextStyle(fontSize = 18.sp), textAlign = TextAlign.Center)
        }
        Spacer(Modifier.width(8.dp))
        Text(
            player.name,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(end = 4.dp)
        )
        Spacer(Modifier.width(4.dp))
        Box(
            Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Close,
                "Retirer ${player.name}",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
