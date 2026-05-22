package fr.erban.dxitcompanion.stats.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.erban.dxitcompanion.databinding.StatsBinding
import fr.erban.dxitcompanion.db.player.PlayerEntity
import fr.erban.dxitcompanion.db.player.PlayerViewModel

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: StatsBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        playerViewModel.players.observe(this) { players -> populateTable(players) }
    }

    private fun populateTable(players: List<PlayerEntity>) {
        players.forEachIndexed { index, player ->
            val row = TableRow(this).apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }
            listOf(
                player.name,
                player.nbGames.toString(),
                player.nbWins.toString(),
                if (player.nbGames != 0) (100 * player.nbWins / player.nbGames).toString() else "0"
            ).forEach { value ->
                row.addView(TextView(this).apply {
                    text = value
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    gravity = Gravity.CENTER
                })
            }
            binding.playerStatsTable.addView(row, index + 1)
        }
    }

    private fun sortByColumn(columnIndex: Int, numeric: Boolean) {
        val table = binding.playerStatsTable
        val rows = (1 until table.childCount).map { table.getChildAt(it) as TableRow }
        val sorted = if (numeric) {
            rows.sortedByDescending { (it.getChildAt(columnIndex) as TextView).text.toString().toDoubleOrNull() ?: 0.0 }
        } else {
            rows.sortedBy { (it.getChildAt(columnIndex) as TextView).text.toString() }
        }
        table.removeViews(1, table.childCount - 1)
        sorted.forEach { table.addView(it) }
    }

    fun sortNumbers(view: View) {
        sortByColumn((view.parent as TableRow).indexOfChild(view), numeric = true)
    }

    fun sortNames(view: View) {
        sortByColumn((view.parent as TableRow).indexOfChild(view), numeric = false)
    }
}
