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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import fr.erban.dxitcompanion.ui.theme.DeepNight
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer

@Composable
fun SelectStoryTellerScreen(
    players: List<PlayerBean>,
    turnNumber: Int,
    onContinue: (PlayerBean) -> Unit
) {
    var selected by remember { mutableStateOf<PlayerBean?>(null) }

    DixitScaffold(title = "Qui raconte ?", turnNumber = turnNumber) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(players) { player ->
                val isSelected = selected?.name == player.name
                Card(
                    onClick = { selected = player },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PurpleContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selected = player },
                            colors = RadioButtonDefaults.colors(selectedColor = Purple)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isSelected) Purple else DeepNight
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "${player.currentScore} pts",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) Purple else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomCTA("Confirmer", enabled = selected != null) { selected?.let { onContinue(it) } }
    }
}
