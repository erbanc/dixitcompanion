package fr.erban.dxitcompanion.db.game

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import fr.erban.dxitcompanion.db.DxitDatabase
import fr.erban.dxitcompanion.db.enums.FirebaseReferencesEnum
import fr.erban.dxitcompanion.game.GameBean
import kotlinx.coroutines.tasks.await
import java.util.Calendar

internal class GameRepository(application: Application) {

    private val gameDao = DxitDatabase.getDatabase(application).gameDao()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    suspend fun insert(game: GameBean) {
        gameDao.insert(GameConverter.toEntity(game))
        try {
            val installationId = FirebaseInstallations.getInstance().id.await()
            val timestamp = Calendar.getInstance().time.toString()
            firebaseDatabase
                .getReference(installationId)
                .child(FirebaseReferencesEnum.GAMES.name)
                .child(timestamp)
                .setValue(game)
        } catch (e: Exception) {
            Log.w("Firebase Error", "Error during saving game")
        }
    }
}
