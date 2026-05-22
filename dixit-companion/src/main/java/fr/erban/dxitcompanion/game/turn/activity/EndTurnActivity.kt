package fr.erban.dxitcompanion.game.turn.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.EndTurnBinding
import fr.erban.dxitcompanion.db.game.GameViewModel
import fr.erban.dxitcompanion.db.player.PlayerConverter
import fr.erban.dxitcompanion.db.player.PlayerViewModel
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.activity.ScoresResultActivity
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.player.TurnScore
import fr.erban.dxitcompanion.game.turn.ScoreRow
import fr.erban.dxitcompanion.game.turn.Turn
import fr.erban.dxitcompanion.game.turn.adapter.PointsTotalAdapter
import fr.erban.dxitcompanion.game.turn.bean.VoteBean

class EndTurnActivity : AppCompatActivity() {

    private lateinit var binding: EndTurnBinding
    private lateinit var turn: Turn
    private lateinit var gameBean: GameBean
    private var endGameReached = false
    private var winner: PlayerBean? = null
    private lateinit var gameViewModel: GameViewModel
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EndTurnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        turn = intent.parcelableExtra("Turn")!!
        gameBean = intent.parcelableExtra("Game")!!

        val updatedPlayers = gameBean.players
            .map { getPlayerResults(it, turn.votes) }
            .sortedByDescending { it.currentScore }

        gameBean = gameBean.copy(players = updatedPlayers)

        val scores = updatedPlayers.map { ScoreRow(it.name, "${it.currentScore} (+${it.scoreLastTurn})") }
        binding.listViewEndTurn.adapter = PointsTotalAdapter(this, scores)
        binding.turnNumber.text = getString(R.string.turnNumberPrefix) + gameBean.currentTurn

        val turnLimitReached = gameBean.currentTurn >= gameBean.maxTurns
        for (player in updatedPlayers) {
            if (player.currentScore >= gameBean.pointsToWin || turnLimitReached) {
                when {
                    winner == null -> { winner = player; endGameReached = true }
                    player.currentScore > winner!!.currentScore -> { winner = player; endGameReached = true }
                    player.currentScore == winner!!.currentScore -> endGameReached = false
                }
            }
        }

        if (endGameReached) {
            binding.endTurn.text = "${winner!!.name} ${getString(R.string.wonTheGame)}"
        }
    }

    private fun getPlayerResults(player: PlayerBean, votes: List<VoteBean>): PlayerBean {
        val votesForCard = votes.count { it.elected.name == player.name }
        val hasFoundCard = votes.any { it.voter.name == player.name && it.elected.name == turn.storyTeller!!.name }

        val lastTurnScore = when {
            player.name == turn.storyTeller!!.name -> if (turn.noOneFound || turn.everybodyFound) 0 else 3
            turn.noOneFound || turn.everybodyFound -> 2  // flat 2, no vote counting
            else -> (if (hasFoundCard) 3 else 0) + votesForCard
        }

        val updatedScore = player.currentScore + lastTurnScore
        return player.copy(
            currentScore = updatedScore,
            scoreLastTurn = lastTurnScore,
            scoresheet = player.scoresheet + TurnScore(turn = gameBean.currentTurn, score = updatedScore)
        )
    }

    fun continueToEndOrNewTurn(view: View) {
        if (endGameReached) continueToEndGame() else continueToNewTurn()
    }

    private fun continueToEndGame() {
        val updatedPlayers = gameBean.players.map { player ->
            if (player.name == winner!!.name)
                player.copy(nbWins = player.nbWins + 1, nbGames = player.nbGames + 1)
            else
                player.copy(nbGames = player.nbGames + 1)
        }
        gameBean = gameBean.copy(finished = true, nameWinner = winner!!.name, players = updatedPlayers)
        gameViewModel.insert(gameBean)
        playerViewModel.update(PlayerConverter.toEntities(gameBean.players))
        startActivity(Intent(this, ScoresResultActivity::class.java).putExtra("Game", gameBean))
    }

    private fun continueToNewTurn() {
        startActivity(Intent(this, SelectStoryTellerActivity::class.java).putExtra("Game", gameBean))
    }
}
