package fr.erban.dxitcompanion.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.Gold
import fr.erban.dxitcompanion.ui.theme.Mint
import fr.erban.dxitcompanion.ui.theme.Purple

val chipColors = listOf(
    Purple,
    Mint,
    Coral,
    Gold,
    Color(0xFF8B6FCE),
    Color(0xFF3A9E7A)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectPlayersScreen(onContinue: (List<String>) -> Unit) {
    var input by remember { mutableStateOf("") }
    var players by remember { mutableStateOf(listOf<String>()) }

    DixitScaffold(title = "Joueurs") {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        label = { Text("Nom du joueur") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Purple,
                            focusedLabelColor = Purple
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = {
                            if (input.isNotBlank() && !players.contains(input.trim())) {
                                players = players + input.trim()
                                input = ""
                            }
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple)
                    ) {
                        Icon(Icons.Default.Add, "Ajouter", tint = Color.White)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(players.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    players.forEachIndexed { index, name ->
                        val col = chipColors[index % chipColors.size]
                        InputChip(
                            selected = false,
                            onClick = { players = players - name },
                            label = { Text(name, color = Color.White) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    "Retirer",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(
                                containerColor = col,
                                selectedContainerColor = col
                            )
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            if (players.size < 3) {
                Text(
                    "Minimum 3 joueurs requis",
                    style = MaterialTheme.typography.bodySmall,
                    color = Coral,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomCTA("Continuer →", enabled = players.size >= 3) { onContinue(players) }
    }
}
