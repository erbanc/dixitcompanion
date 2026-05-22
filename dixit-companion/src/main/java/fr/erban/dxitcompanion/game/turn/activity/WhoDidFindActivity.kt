package fr.erban.dxitcompanion.game.turn.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.WhoDidFindBinding
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.SelectPlayerRow
import fr.erban.dxitcompanion.game.turn.Turn
import fr.erban.dxitcompanion.game.turn.adapter.PlayerSelectionAdapter
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import fr.erban.dxitcompanion.game.turn.bean.VotesBean

class WhoDidFindActivity : AppCompatActivity() {

    private lateinit var binding: WhoDidFindBinding
    private lateinit var turn: Turn
    private lateinit var gameBean: GameBean
    private val playersWhoFound = mutableListOf<PlayerBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WhoDidFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        turn = intent.parcelableExtra("Turn")!!
        gameBean = intent.parcelableExtra("Game")!!

        binding.turnNumber.text = getString(R.string.turnNumberPrefix) + gameBean.currentTurn

        val rows = gameBean.players
            .filter { it.name != turn.storyTeller?.name }
            .map { SelectPlayerRow(name = it.name, checked = false) }

        binding.listViewSelectPlayer.adapter = PlayerSelectionAdapter(this, rows)
    }

    fun clickCheckbox(view: View) {
        val checkBox = view as CheckBox
        val layout = checkBox.parent as RelativeLayout
        val name = (layout.getChildAt(0) as TextView).text.toString()
        val player = gameBean.players.firstOrNull { it.name == name } ?: return
        if (checkBox.isChecked) playersWhoFound.add(player) else playersWhoFound.remove(player)
    }

    fun continueToSelectVotes(view: View) {
        if (playersWhoFound.size == gameBean.players.size - 1) {
            turn = turn.copy(everybodyFound = true)
            startActivity(
                Intent(this, EndTurnActivity::class.java)
                    .putExtra("Game", gameBean)
                    .putExtra("Turn", turn)
            )
        } else {
            val initialVotes = playersWhoFound.map { VoteBean(voter = it, elected = turn.storyTeller!!) }
            if (playersWhoFound.isEmpty()) turn = turn.copy(noOneFound = true)
            startActivity(
                Intent(this, SelectVotesActivity::class.java)
                    .putExtra("Game", gameBean)
                    .putExtra("Turn", turn)
                    .putExtra("Votes", VotesBean(votes = initialVotes))
            )
        }
    }
}
