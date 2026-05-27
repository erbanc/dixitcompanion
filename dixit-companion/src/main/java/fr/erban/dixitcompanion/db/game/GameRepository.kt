package fr.erban.dixitcompanion.db.game

import android.app.Application
import fr.erban.dixitcompanion.db.DxitDatabase
import fr.erban.dixitcompanion.game.GameBean

internal class GameRepository(application: Application) {

    private val gameDao = DxitDatabase.getDatabase(application).gameDao()

    suspend fun insert(game: GameBean) {
        gameDao.insert(GameConverter.toEntity(game))
    }
}
