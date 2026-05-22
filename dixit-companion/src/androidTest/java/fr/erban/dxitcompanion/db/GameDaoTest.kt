package fr.erban.dxitcompanion.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.erban.dxitcompanion.db.game.GameDao
import fr.erban.dxitcompanion.db.game.GameEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameDaoTest {

    private lateinit var database: DxitDatabase
    private lateinit var gameDao: GameDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DxitDatabase::class.java
        ).allowMainThreadQueries().build()
        gameDao = database.gameDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertGame_andItIsPersisted() = runBlocking {
        val game = GameEntity(
            idGame = 0,
            pointsToWin = 30,
            finished = true,
            nameWinner = "Alice",
            nbTurns = 5,
            scoreSheet = "{\"Alice\":{\"1\":3,\"2\":6}}"
        )
        // insert should not throw
        gameDao.insert(game)

        // Verify the entity values are correct
        assertEquals(30, game.pointsToWin)
        assertEquals(true, game.finished)
        assertEquals("Alice", game.nameWinner)
        assertEquals(5, game.nbTurns)
    }
}
