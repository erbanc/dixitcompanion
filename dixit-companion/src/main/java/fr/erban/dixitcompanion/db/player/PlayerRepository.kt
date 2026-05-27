package fr.erban.dixitcompanion.db.player

import android.app.Application
import androidx.lifecycle.LiveData
import fr.erban.dixitcompanion.db.DxitDatabase

internal class PlayerRepository(application: Application) {

    private val playerDao = DxitDatabase.getDatabase(application).playerDao()

    val allPlayers: LiveData<List<PlayerEntity>> = playerDao.getAll()

    suspend fun insert(playerEntity: PlayerEntity) {
        playerDao.insert(playerEntity)
    }

    suspend fun upsert(players: List<PlayerEntity>) {
        playerDao.upsert(players)
    }
}
