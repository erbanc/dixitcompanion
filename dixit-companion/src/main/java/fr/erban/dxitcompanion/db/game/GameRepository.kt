package fr.erban.dxitcompanion.db.game

import android.app.Application
import fr.erban.dxitcompanion.db.DxitDatabase
import fr.erban.dxitcompanion.game.GameBean

internal class GameRepository(application: Application) {

    private val gameDao = DxitDatabase.getDatabase(application).gameDao()

    suspend fun insert(game: GameBean) {
        gameDao.insert(GameConverter.toEntity(game))
    }
}
