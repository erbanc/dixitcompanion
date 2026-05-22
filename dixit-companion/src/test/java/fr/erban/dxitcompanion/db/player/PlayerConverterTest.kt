package fr.erban.dxitcompanion.db.player

import fr.erban.dxitcompanion.game.player.PlayerBean
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerConverterTest {

    @Test
    fun roundTrip_playerBeanToEntityAndBack_preservesAllFields() {
        val original = PlayerBean(
            name = "Alice",
            currentScore = 15,
            nbGames = 5,
            nbWins = 2,
            persisted = false
        )
        val entity = PlayerConverter.toEntity(original)
        val restored = PlayerConverter.toBean(entity)

        assertEquals(original.name, restored.name)
        assertEquals(original.nbGames, restored.nbGames)
        assertEquals(original.nbWins, restored.nbWins)
        // currentScore resets to 0 when loading from DB (by design)
        assertEquals(0, restored.currentScore)
        // persisted flag is set to true when loaded from DB
        assertTrue(restored.persisted)
        // scoresheet is empty when loaded from DB
        assertTrue(restored.scoresheet.isEmpty())
    }

    @Test
    fun toEntities_convertsListCorrectly() {
        val players = listOf(
            PlayerBean(name = "Alice", nbGames = 3, nbWins = 1),
            PlayerBean(name = "Bob", nbGames = 5, nbWins = 2),
            PlayerBean(name = "Charlie", nbGames = 1, nbWins = 0)
        )
        val entities = PlayerConverter.toEntities(players)

        assertEquals(3, entities.size)
        assertEquals("Alice", entities[0].name)
        assertEquals(3, entities[0].nbGames)
        assertEquals(1, entities[0].nbWins)
        assertEquals("Bob", entities[1].name)
        assertEquals(5, entities[1].nbGames)
        assertEquals(2, entities[1].nbWins)
        assertEquals("Charlie", entities[2].name)
        assertEquals(1, entities[2].nbGames)
        assertEquals(0, entities[2].nbWins)
    }
}
