package fr.erban.dxitcompanion.game.turn

import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.player.TurnScore
import fr.erban.dxitcompanion.game.turn.bean.VoteBean

object ScoringEngine {

    fun computeScore(player: PlayerBean, turn: Turn, currentTurn: Int): PlayerBean {
        val votes = turn.votes
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
            scoresheet = player.scoresheet + TurnScore(turn = currentTurn, score = updatedScore)
        )
    }
}
