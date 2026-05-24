package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.components.PlayerAvatar
import fr.erban.dxitcompanion.ui.components.TurnStep
import fr.erban.dxitcompanion.ui.components.rememberHaptic
import fr.erban.dxitcompanion.ui.components.confirm
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.Mint
import fr.erban.dxitcompanion.ui.theme.toColorOrDefault

@Composable
fun EveryoneFoundScreen(
    turnNumber: Int,
    storyteller: PlayerBean?,
    onAnswer: (Boolean) -> Unit
) {
    DixitScaffold(
        title = "Tout le monde\na trouvé ?",
        turnNumber = turnNumber,
        titleLines = 2,
        step = TurnStep.EveryoneFound
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            storyteller?.let { st ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlayerAvatar(emoji = st.emoji, color = st.colorHex.toColorOrDefault(), size = 32.dp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Carte de ${st.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BigChoiceButton("OUI", Mint, Color.White, Modifier.weight(1f)) { onAnswer(true) }
                BigChoiceButton("NON", Coral, Color.White, Modifier.weight(1f)) { onAnswer(false) }
            }
        }
    }
}

@Composable
private fun BigChoiceButton(
    label: String,
    color: Color,
    textColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val haptic = rememberHaptic()
    Button(
        onClick = { haptic.confirm(); onClick() },
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = textColor),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.displayMedium)
    }
}
