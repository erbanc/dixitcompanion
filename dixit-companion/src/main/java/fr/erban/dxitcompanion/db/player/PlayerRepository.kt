package fr.erban.dxitcompanion.db.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import fr.erban.dxitcompanion.db.DxitDatabase
import fr.erban.dxitcompanion.db.enums.FirebaseReferencesEnum
import kotlinx.coroutines.tasks.await

internal class PlayerRepository(application: Application) {

    private val playerDao = DxitDatabase.getDatabase(application).playerDao()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    val allPlayers: LiveData<List<PlayerEntity>> = playerDao.getAll()

    suspend fun insert(playerEntity: PlayerEntity) {
        playerDao.insert(playerEntity)
        try {
            val installationId = FirebaseInstallations.getInstance().id.await()
            firebaseDatabase
                .getReference(installationId)
                .child(FirebaseReferencesEnum.PLAYERS.name)
                .child(playerEntity.name)
                .setValue(playerEntity)
        } catch (e: Exception) {
            Log.w("Firebase Error", "Error during saving player")
        }
    }

    suspend fun update(players: List<PlayerEntity>) {
        playerDao.update(players)
        try {
            val installationId = FirebaseInstallations.getInstance().id.await()
            val updates = players.associate { it.name to it as Any }
            firebaseDatabase
                .getReference(installationId)
                .child(FirebaseReferencesEnum.PLAYERS.name)
                .updateChildren(updates)
        } catch (e: Exception) {
            Log.w("Firebase Error", "Error during updating players")
        }
    }
}
