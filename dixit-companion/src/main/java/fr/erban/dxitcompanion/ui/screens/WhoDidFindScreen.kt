package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.components.PlayerAvatar
import fr.erban.dxitcompanion.ui.components.TurnStep
import fr.erban.dxitcompanion.ui.components.rememberHaptic
import fr.erban.dxitcompanion.ui.components.tap
import fr.erban.dxitcompanion.ui.theme.toColorOrDefault

@Composable
fun WhoDidFindScreen(
    players: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (List<PlayerBean>) -> Unit
) {
    var found by remember { mutableStateOf(setOf<String>()) }
    val haptic = rememberHaptic()

    DixitScaffold(
        title = "Qui a trouvé ?",
        turnNumber = turnNumber,
        subtitle = "${found.size} sur ${players.size}",
        step = TurnStep.WhoFound
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(players) { player ->
                val checked = player.name in found
                val color = player.colorHex.toColorOrDefault()
                Card(
                    onClick = {
                        found = if (checked) found - player.name else found + player.name
                        haptic.tap()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = if (checked) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(if (checked) 6.dp else 2.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { c ->
                                found = if (c) found + player.name else found - player.name
                                haptic.tap()
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(Modifier.width(4.dp))
                        PlayerAvatar(emoji = player.emoji, color = color, size = 40.dp)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BottomCTA("Continuer") { onContinue(players.filter { it.name in found }) }
        }
    }
}
