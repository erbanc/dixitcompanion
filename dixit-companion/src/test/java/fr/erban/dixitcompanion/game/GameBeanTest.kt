package fr.erban.dixitcompanion.game

import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.game.player.TurnScore
import org.junit.Assert.assertEquals
import org.junit.Test

class GameBeanTest {

    @Test
    fun getScoresheet_returnsAllPlayersScores() {
        val alice = PlayerBean(
            name = "Alice",
            currentScore = 5,
            scoresheet = listOf(TurnScore(turn = 1, score = 3), TurnScore(turn = 2, score = 5))
        )
        val bob = PlayerBean(
            name = "Bob",
            currentScore = 2,
            scoresheet = listOf(TurnScore(turn = 1, score = 2))
        )
        val gameBean = GameBean(players = listOf(alice, bob))
        val scoresheet = gameBean.getScoresheet()

        assertEquals(2, scoresheet.size)
        assertEquals(mapOf("1" to 3, "2" to 5), scoresheet["Alice"])
        assertEquals(mapOf("1" to 2), scoresheet["Bob"])
    }

    @Test
    fun pointsToWin_defaultsToMaxInt_whenTurnBased() {
        // Turn-based game: pointsToWin = Int.MAX_VALUE, maxTurns = N
        val gameBean = GameBean(
            players = emptyList(),
            pointsToWin = Int.MAX_VALUE,
            maxTurns = 10
        )
        assertEquals(Int.MAX_VALUE, gameBean.pointsToWin)
        assertEquals(10, gameBean.maxTurns)
    }

    @Test
    fun maxTurns_defaultsToMaxInt_whenPointsBased() {
        // Points-based game: maxTurns = Int.MAX_VALUE, pointsToWin = N
        val gameBean = GameBean(
            players = emptyList(),
            pointsToWin = 30,
            maxTurns = Int.MAX_VALUE
        )
        assertEquals(30, gameBean.pointsToWin)
        assertEquals(Int.MAX_VALUE, gameBean.maxTurns)
    }
}
