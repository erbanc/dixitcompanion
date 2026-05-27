package fr.erban.dxitcompanion.db.player

import fr.erban.dxitcompanion.game.player.PlayerBean
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerConverterTest {

    @Test
    fun toBean_preservesAllStatsFields() {
        val entity = PlayerEntity(
            name = "Alice",
            nbGames = 5, nbWins = 2,
            nbAsStoryteller = 3, nbAsVoter = 10, nbFoundStoryteller = 7,
            nbStorytellerOptimalTurns = 2, totalPoints = 48
        )
        val bean = PlayerConverter.toBean(entity)

        assertEquals("Alice", bean.name)
        assertEquals(5, bean.nbGames)
        assertEquals(2, bean.nbWins)
        assertEquals(3, bean.nbAsStoryteller)
        assertEquals(10, bean.nbAsVoter)
        assertEquals(7, bean.nbFoundStoryteller)
        assertEquals(2, bean.nbStorytellerOptimalTurns)
        assertEquals(48, bean.totalPoints)
        assertEquals(0, bean.currentScore)
        assertTrue(bean.persisted)
        assertTrue(bean.scoresheet.isEmpty())
    }

    @Test
    fun toEndGameEntities_incrementsGamesWins_accumulatesPoints() {
        val players = listOf(
            PlayerBean(name = "Alice", nbGames = 3, nbWins = 1, currentScore = 15,
                nbAsStoryteller = 2, nbAsVoter = 6, nbFoundStoryteller = 4,
                nbStorytellerOptimalTurns = 1, totalPoints = 30),
            PlayerBean(name = "Bob", nbGames = 3, nbWins = 0, currentScore = 10,
                nbAsStoryteller = 1, nbAsVoter = 7, nbFoundStoryteller = 3,
                nbStorytellerOptimalTurns = 0, totalPoints = 25)
        )
        val entities = PlayerConverter.toEndGameEntities(players, winnerName = "Alice")

        val alice = entities.first { it.name == "Alice" }
        assertEquals(4, alice.nbGames)
        assertEquals(2, alice.nbWins)
        assertEquals(45, alice.totalPoints)          // 30 + 15
        assertEquals(1, alice.nbStorytellerOptimalTurns)

        val bob = entities.first { it.name == "Bob" }
        assertEquals(4, bob.nbGames)
        assertEquals(0, bob.nbWins)
        assertEquals(35, bob.totalPoints)            // 25 + 10
    }

    @Test
    fun toEndGameEntities_noWinner_noWinIncrement() {
        val players = listOf(PlayerBean(name = "Alice", nbGames = 1, nbWins = 0, currentScore = 8, totalPoints = 0))
        val entities = PlayerConverter.toEndGameEntities(players, winnerName = null)
        assertEquals(0, entities[0].nbWins)
        assertEquals(2, entities[0].nbGames)
        assertEquals(8, entities[0].totalPoints)
    }
}
