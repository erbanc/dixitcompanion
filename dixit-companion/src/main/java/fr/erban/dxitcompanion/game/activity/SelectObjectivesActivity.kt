package fr.erban.dxitcompanion.game.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.SelectObjectivesBinding
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.turn.activity.SelectStoryTellerActivity

class SelectObjectivesActivity : AppCompatActivity() {

    private lateinit var binding: SelectObjectivesBinding
    private var pointsToWinToggled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectObjectivesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameBean = intent.parcelableExtra<GameBean>("Game")!!
        binding.pointsToWin.setText("30")
        binding.turnsToWin.setText((2 * gameBean.players.size).toString())

        updateLayoutsVisibility()

        binding.switchObjective.setOnClickListener {
            pointsToWinToggled = !pointsToWinToggled
            updateLayoutsVisibility()
        }
    }

    private fun updateLayoutsVisibility() {
        binding.pointBasedLayout.visibility = if (pointsToWinToggled) View.VISIBLE else View.INVISIBLE
        binding.turnBasedLayout.visibility = if (pointsToWinToggled) View.INVISIBLE else View.VISIBLE
    }

    fun continueToFirstTurn(view: View) {
        val gameBean = intent.parcelableExtra<GameBean>("Game") ?: return
        val updatedGame = if (pointsToWinToggled) {
            val nb = binding.pointsToWin.text.toString().toIntOrNull()?.takeIf { it > 0 } ?: run {
                Toast.makeText(this, getString(R.string.enterANumberOfPointsToWin), Toast.LENGTH_SHORT).show()
                return
            }
            gameBean.copy(pointsToWin = nb, maxTurns = Int.MAX_VALUE)
        } else {
            val nb = binding.turnsToWin.text.toString().toIntOrNull()?.takeIf { it > 0 } ?: run {
                Toast.makeText(this, getString(R.string.enterANumberOfTurnsToWin), Toast.LENGTH_SHORT).show()
                return
            }
            gameBean.copy(pointsToWin = Int.MAX_VALUE, maxTurns = nb)
        }
        startActivity(Intent(this, SelectStoryTellerActivity::class.java).putExtra("Game", updatedGame))
    }
}
