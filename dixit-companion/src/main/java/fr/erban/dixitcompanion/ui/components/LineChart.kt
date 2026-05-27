package fr.erban.dixitcompanion.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.erban.dixitcompanion.ui.theme.toColorOrDefault
import androidx.compose.foundation.background

data class ChartSeries(
    val label: String,
    val color: Color,
    val points: List<Pair<Int, Int>> // (turn, score)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DixitLineChart(
    series: List<ChartSeries>,
    maxTurn: Int,
    maxScore: Int,
    modifier: Modifier = Modifier
) {
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
        label = "chart-anim"
    )

    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outline = MaterialTheme.colorScheme.outlineVariant
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelSmall.copy(
        color = onSurfaceVariant,
        fontSize = 11.sp
    )

    Column(modifier) {
        Box(Modifier.weight(1f).fillMaxWidth()) {
            Canvas(Modifier.fillMaxSize().padding(start = 32.dp, top = 8.dp, end = 8.dp, bottom = 24.dp)) {
                val w = size.width
                val h = size.height
                val safeMaxScore = maxOf(maxScore, 1)
                val safeMaxTurn = maxOf(maxTurn, 1)

                // Grid lines (horizontal)
                val ySteps = 4
                for (i in 0..ySteps) {
                    val y = h - (h * i / ySteps)
                    drawLine(
                        outline.copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                    val scoreLabel = (safeMaxScore * i / ySteps).toString()
                    val layout = textMeasurer.measure(scoreLabel, labelStyle)
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(-layout.size.width - 6f, y - layout.size.height / 2f)
                    )
                }

                // X-axis labels (turns)
                val xSteps = minOf(safeMaxTurn, 5)
                for (i in 0..xSteps) {
                    val turn = (safeMaxTurn * i / xSteps)
                    val x = w * turn / safeMaxTurn.toFloat()
                    val xLabel = turn.toString()
                    val layout = textMeasurer.measure(xLabel, labelStyle)
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(x - layout.size.width / 2f, h + 6f)
                    )
                }

                // Series
                series.forEach { s ->
                    if (s.points.isEmpty()) return@forEach
                    val allPoints = listOf(0 to 0) + s.points.sortedBy { it.first }
                    val path = Path()
                    allPoints.forEachIndexed { index, (turn, score) ->
                        val x = w * turn / safeMaxTurn.toFloat()
                        val y = h - (h * score / safeMaxScore.toFloat())
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    val animatedPath = Path()
                    val measure = android.graphics.PathMeasure(path.asAndroidPath(), false)
                    val length = measure.length
                    measure.getSegment(0f, length * animationProgress, animatedPath.asAndroidPath(), true)

                    drawPath(
                        path = animatedPath,
                        color = s.color,
                        style = Stroke(
                            width = 5f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                    // Endpoint dot
                    val last = allPoints.last()
                    val lx = w * last.first / safeMaxTurn.toFloat()
                    val ly = h - (h * last.second / safeMaxScore.toFloat())
                    if (animationProgress >= 0.95f) {
                        drawCircle(s.color, radius = 6f, center = Offset(lx, ly))
                        drawCircle(Color.White, radius = 3f, center = Offset(lx, ly))
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        // Legend
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            series.forEach { s ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(s.color)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        s.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = onSurface
                    )
                }
            }
        }
    }
}
