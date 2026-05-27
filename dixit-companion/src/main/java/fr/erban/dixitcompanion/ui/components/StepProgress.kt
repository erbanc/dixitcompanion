package fr.erban.dixitcompanion.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

enum class TurnStep { Storyteller, EveryoneFound, WhoFound, Votes, EndTurn }

@Composable
fun TurnStepIndicator(
    current: TurnStep,
    modifier: Modifier = Modifier
) {
    val steps = TurnStep.values()
    val currentIdx = steps.indexOf(current)
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        steps.forEachIndexed { index, _ ->
            val active = index <= currentIdx
            val color by animateColorAsState(
                targetValue = if (active) MaterialTheme.colorScheme.primary
                              else MaterialTheme.colorScheme.outlineVariant,
                label = "step-color"
            )
            androidx.compose.foundation.layout.Box(
                Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}
