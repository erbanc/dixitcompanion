package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Coral
import fr.erban.dxitcompanion.ui.theme.Mint

@Composable
fun EveryoneFoundScreen(
    turnNumber: Int,
    storytellerName: String,
    onAnswer: (Boolean) -> Unit
) {
    DixitScaffold(
        title = "Tout le monde\na trouvé ?",
        turnNumber = turnNumber,
        titleLines = 2,
        subtitle = "Carte de $storytellerName"
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BigChoiceButton("OUI", Mint, Color.White, Modifier.weight(1f)) { onAnswer(true) }
            BigChoiceButton("NON", Coral, Color.White, Modifier.weight(1f)) { onAnswer(false) }
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
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = textColor),
        elevation = ButtonDefaults.buttonElevation(6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.displayMedium)
    }
}
