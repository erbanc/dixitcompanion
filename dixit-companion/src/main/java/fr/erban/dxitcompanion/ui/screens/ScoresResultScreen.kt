package fr.erban.dxitcompanion.ui.screens

import android.graphics.Color as AndroidColor
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.ui.components.BottomCTA
import fr.erban.dxitcompanion.ui.components.DixitScaffold
import kotlin.random.Random

@Composable
fun ScoresResultScreen(game: GameBean, onHome: () -> Unit) {
    val maxPoints = game.players.maxOfOrNull { it.currentScore } ?: 0
    val hasData = game.players.isNotEmpty() && game.players.any { it.scoresheet.isNotEmpty() }

    DixitScaffold(title = "Évolution ✦") {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                if (!hasData) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Aucune donnée à afficher",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    AndroidView(
                        factory = { ctx ->
                            GraphView(ctx).apply {
                                viewport.isScalable = true
                                viewport.isScrollable = true
                                viewport.isYAxisBoundsManual = true
                                viewport.setMinY(0.0)
                                viewport.setMaxY((maxPoints + 1).toDouble())
                                viewport.isXAxisBoundsManual = true
                                viewport.setMinX(0.0)
                                viewport.setMaxX(game.currentTurn.toDouble())
                                legendRenderer.isVisible = true
                                game.players.forEach { player ->
                                    if (player.scoresheet.isNotEmpty()) {
                                        val series = LineGraphSeries<DataPoint>().apply {
                                            appendData(DataPoint(0.0, 0.0), true, game.currentTurn + 2)
                                            player.scoresheet.forEach { ts ->
                                                appendData(
                                                    DataPoint(ts.turn.toDouble(), ts.score.toDouble()),
                                                    true,
                                                    game.currentTurn + 2
                                                )
                                            }
                                            color = AndroidColor.argb(
                                                255,
                                                Random.nextInt(256),
                                                Random.nextInt(256),
                                                Random.nextInt(256)
                                            )
                                            title = player.name
                                            thickness = 8
                                        }
                                        addSeries(series)
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            BottomCTA("Retour à l'accueil") { onHome() }
        }
    }
}
