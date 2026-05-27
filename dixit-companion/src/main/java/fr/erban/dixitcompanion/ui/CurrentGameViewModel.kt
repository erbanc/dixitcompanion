package fr.erban.dixitcompanion.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fr.erban.dixitcompanion.game.GameBean
import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.game.turn.ScoringEngine
import fr.erban.dixitcompanion.game.turn.Turn
import fr.erban.dixitcompanion.game.turn.bean.VoteBean

class CurrentGameViewModel : ViewModel() {
    var game by mutableStateOf(GameBean(players = emptyList()))
        private set
    var turn by mutableStateOf(Turn())
        private set

    /** Players carried over from the last finished game, kept in memory for "rematch". */
    var rematchTemplate by mutableStateOf<List<PlayerBean>?>(null)
        private set

    fun startGame(players: List<PlayerBean>, pointsToWin: Int, maxTurns: Int) {
        game = GameBean(
            players = players,
            pointsToWin = pointsToWin,
            maxTurns = maxTurns,
            currentTurn = 1,
            startedAt = System.currentTimeMillis()
        )
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
        turn = turn.copy(noOneFound = found.isEmpty(), whoFound = found)
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
        val endedAt = if (endGame) System.currentTimeMillis() else game.endedAt
        game = game.copy(
            players = updatedPlayers,
            nameWinner = if (endGame) winner?.name else null,
            finished = endGame,
            endedAt = endedAt
        )
        if (endGame) {
            // Snapshot players (without scores) for a possible rematch
            rematchTemplate = updatedPlayers.map { p ->
                PlayerBean(
                    name = p.name,
                    colorHex = p.colorHex,
                    emoji = p.emoji,
                    persisted = p.persisted,
                    nbGames = p.nbGames,
                    nbWins = p.nbWins
                )
            }
        }
        return Pair(game, endGame)
    }

    fun advanceTurn() {
        game = game.copy(currentTurn = game.currentTurn + 1)
        turn = Turn()
    }

    fun resetGame() {
        game = GameBean(players = emptyList())
        turn = Turn()
        rematchTemplate = null
    }

    fun consumeRematchTemplate(): List<PlayerBean>? {
        val tpl = rematchTemplate
        rematchTemplate = null
        return tpl
    }
}
