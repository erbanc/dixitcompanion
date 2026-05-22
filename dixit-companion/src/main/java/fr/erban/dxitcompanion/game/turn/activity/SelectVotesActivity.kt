package fr.erban.dxitcompanion.game.turn.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.SelectVotesBinding
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.Turn
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import fr.erban.dxitcompanion.game.turn.bean.VotesBean

class SelectVotesActivity : AppCompatActivity() {

    private lateinit var binding: SelectVotesBinding
    private lateinit var turn: Turn
    private lateinit var gameBean: GameBean
    private var votes = VotesBean()
    private var voter: PlayerBean? = null
    private val alreadyVoted = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectVotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        turn = intent.parcelableExtra("Turn")!!
        gameBean = intent.parcelableExtra("Game")!!
        votes = intent.parcelableExtra("Votes") ?: VotesBean()

        alreadyVoted.add(turn.storyTeller!!.name)
        votes.votes.forEach { alreadyVoted.add(it.voter.name) }

        binding.turnNumber.text = getString(R.string.turnNumberPrefix) + gameBean.currentTurn
        reinitActivity()
    }

    private fun reinitActivity() {
        val nextVoter = gameBean.players.firstOrNull { it.name !in alreadyVoted }
        if (nextVoter == null) return
        voter = nextVoter

        binding.selectVotesTitle.apply {
            text = "Pour qui a voté ${nextVoter.name} ?"
            typeface = ResourcesCompat.getFont(this@SelectVotesActivity, R.font.write_me_a_song)
            textSize = 60f
        }

        if (alreadyVoted.size == gameBean.players.size - 1) {
            binding.nextPlayerButton.visibility = View.INVISIBLE
            binding.selectVotesContinueBtn.visibility = View.VISIBLE
        }

        populateRadioGroup()
    }

    private fun populateRadioGroup() {
        val rg = binding.radioGroupSelectVote
        for (i in rg.childCount - 1 downTo 0) {
            if (rg.getChildAt(i) is RadioButton) rg.removeViewAt(i)
        }
        rg.clearCheck()

        gameBean.players
            .filter { it.name != turn.storyTeller?.name && it.name != voter?.name }
            .forEach { player ->
                val rb = RadioButton(this).apply {
                    text = player.name
                    textSize = 50f
                    typeface = ResourcesCompat.getFont(this@SelectVotesActivity, R.font.write_me_a_song)
                    setTextColor(resources.getColor(R.color.backgroundTextColor, theme))
                }
                rg.addView(rb)
                if (rg.checkedRadioButtonId == -1) rb.isChecked = true
            }
    }

    private fun recordCurrentVote() {
        val rg = binding.radioGroupSelectVote
        val rb = rg.findViewById<RadioButton>(rg.checkedRadioButtonId)
        val elected = gameBean.players.first { it.name == rb.text.toString() }
        votes = votes.copy(votes = votes.votes + VoteBean(voter = voter!!, elected = elected))
    }

    fun nextPlayer(view: View) {
        recordCurrentVote()
        alreadyVoted.add(voter!!.name)
        reinitActivity()
    }

    fun continueToEndTurn(view: View) {
        recordCurrentVote()
        turn = turn.copy(votes = votes.votes)
        startActivity(
            Intent(this, EndTurnActivity::class.java)
                .putExtra("Game", gameBean)
                .putExtra("Turn", turn)
        )
    }
}
