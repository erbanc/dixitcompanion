package fr.erban.dxitcompanion.game.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import fr.erban.dxitcompanion.MainActivity
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.ScoresGraphBinding
import fr.erban.dxitcompanion.game.GameBean
import kotlin.random.Random

class ScoresResultActivity : AppCompatActivity() {

    private lateinit var binding: ScoresGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScoresGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameBean = intent.parcelableExtra<GameBean>("Game") ?: return
        val maxPoints = gameBean.players.maxOfOrNull { it.currentScore } ?: 0

        binding.graph.apply {
            isClickable = true
            viewport.isScalable = true
            viewport.isScrollable = true
            viewport.isYAxisBoundsManual = true
            viewport.setMinY(0.0)
            viewport.setMaxY(maxPoints.toDouble())
            viewport.isXAxisBoundsManual = true
            viewport.setMinX(0.0)
            viewport.setMaxX(gameBean.currentTurn.toDouble())
            legendRenderer.isVisible = true
            legendRenderer.setFixedPosition(0, maxPoints)
        }

        gameBean.players.forEach { player ->
            val series = LineGraphSeries<DataPoint>().apply {
                appendData(DataPoint(0.0, 0.0), true, maxPoints)
                player.scoresheet.forEach { ts ->
                    appendData(DataPoint(ts.turn.toDouble(), ts.score.toDouble()), true, maxPoints)
                }
                isAnimated = true
                color = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
                title = player.name
                thickness = 8
            }
            addSeries(series)
        }
    }

    fun continueToHomescreen(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
