package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
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
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer

@Composable
fun WhoDidFindScreen(
    players: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (List<PlayerBean>) -> Unit
) {
    var found by remember { mutableStateOf(setOf<String>()) }

    DixitScaffold(title = "Qui a trouvé ?", turnNumber = turnNumber) {
        Card(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            LazyColumn {
                items(players) { player ->
                    val checked = player.name in found
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { c ->
                                found = if (c) found + player.name else found - player.name
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Purple)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(player.name, style = MaterialTheme.typography.titleLarge)
                    }
                    HorizontalDivider(color = PurpleContainer)
                }
            }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomCTA("Continuer →") { onContinue(players.filter { it.name in found }) }
    }
}
