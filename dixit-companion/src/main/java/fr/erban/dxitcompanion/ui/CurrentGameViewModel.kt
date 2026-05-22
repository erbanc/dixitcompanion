package fr.erban.dxitcompanion.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.ScoringEngine
import fr.erban.dxitcompanion.game.turn.Turn
import fr.erban.dxitcompanion.game.turn.bean.VoteBean

class CurrentGameViewModel : ViewModel() {
    var game by mutableStateOf(GameBean(players = emptyList()))
        private set
    var turn by mutableStateOf(Turn())
        private set

    fun startGame(players: List<PlayerBean>, pointsToWin: Int, maxTurns: Int) {
        game = GameBean(players = players, pointsToWin = pointsToWin, maxTurns = maxTurns, currentTurn = 1)
        turn = Turn()
    }

    fun setStoryteller(player: PlayerBean) {
        turn = Turn(storyTeller = player)
    }

    fun setEveryoneFound(everybodyFound: Boolean) {
        // noOneFound is determined by WhoDidFindScreen when everybodyFound=false;
        // when everybodyFound=true we go directly to EndTurn, so noOneFound must be false.
        turn = turn.copy(everybodyFound = everybodyFound, noOneFound = false)
    }

    fun setWhoFound(found: List<PlayerBean>) {
        turn = turn.copy(noOneFound = found.isEmpty())
    }

    fun setVotes(votes: List<VoteBean>) {
        turn = turn.copy(votes = votes)
    }

    fun computeEndTurn(): Pair<GameBean, Boolean> {
        val updatedPlayers = game.players
            .map { ScoringEngine.computeScore(it, turn, game.currentTurn) }
            .sortedByDescending { it.currentScore }
        var endGame = false
        var winner: PlayerBean? = null
        val turnLimitReached = game.currentTurn >= game.maxTurns
        for (player in updatedPlayers) {
            if (player.currentScore >= game.pointsToWin || turnLimitReached) {
                when {
                    winner == null -> { winner = player; endGame = true }
                    player.currentScore > winner.currentScore -> { winner = player; endGame = true }
                    player.currentScore == winner.currentScore -> endGame = false
                }
            }
        }
        game = game.copy(players = updatedPlayers, nameWinner = if (endGame) winner?.name else null)
        return Pair(game, endGame)
    }

    fun advanceTurn() {
        game = game.copy(currentTurn = game.currentTurn + 1)
        turn = Turn()
    }

    fun resetGame() {
        game = GameBean(players = emptyList())
        turn = Turn()
    }
}
