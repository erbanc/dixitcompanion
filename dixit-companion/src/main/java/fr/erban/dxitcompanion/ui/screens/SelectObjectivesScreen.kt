package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import fr.erban.dxitcompanion.ui.theme.Purple

@Composable
fun SelectObjectivesScreen(onContinue: (pointsToWin: Int, maxTurns: Int) -> Unit) {
    var isPointsMode by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf(30) }

    DixitScaffold(title = "Objectif") {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = isPointsMode,
                    onClick = {
                        isPointsMode = true
                        value = 30
                    },
                    shape = SegmentedButtonDefaults.itemShape(0, 2)
                ) {
                    Text("Points")
                }
                SegmentedButton(
                    selected = !isPointsMode,
                    onClick = {
                        isPointsMode = false
                        value = 5
                    },
                    shape = SegmentedButtonDefaults.itemShape(1, 2)
                ) {
                    Text("Tours")
                }
            }
            Spacer(Modifier.height(32.dp))
            Card(
                Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (isPointsMode) "Points pour gagner" else "Nombre de tours",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "$value",
                        style = MaterialTheme.typography.displayMedium,
                        color = Purple
                    )
                    Slider(
                        value = value.toFloat(),
                        onValueChange = { value = it.toInt() },
                        valueRange = if (isPointsMode) 10f..60f else 3f..20f,
                        steps = if (isPointsMode) 9 else 4,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple,
                            activeTrackColor = Purple
                        )
                    )
                }
            }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomCTA("C'est parti ! ✦") {
            if (isPointsMode) onContinue(value, Int.MAX_VALUE)
            else onContinue(Int.MAX_VALUE, value)
        }
    }
}
