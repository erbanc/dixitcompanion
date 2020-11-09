package fr.erban.dxitcompanion.db.player;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.erban.dxitcompanion.db.DxitDatabase;
import fr.erban.dxitcompanion.db.enums.FirebaseReferencesEnum;

/**
 * The PlayerEntity repository
 */
class PlayerRepository {

    private final PlayerDao playerDao;
    private final LiveData<List<PlayerEntity>> players;
    private final FirebaseDatabase firebaseDatabase;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    PlayerRepository(Application application) {
        DxitDatabase db = DxitDatabase.getDatabase(application);
        playerDao = db.getPlayerDao();
        players = playerDao.getAll();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<PlayerEntity>> getAllPlayers() {
        return players;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(PlayerEntity playerEntity) {
        // First, insert in the local db
        DxitDatabase.databaseWriteExecutor.execute(() -> playerDao.insert(playerEntity));

        try {
            FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {

                if (task.getResult() != null) {
                    DatabaseReference playersReference = firebaseDatabase
                            .getReference(task.getResult())
                            .child(FirebaseReferencesEnum.PLAYERS.name());
                    DatabaseReference playerReference = playersReference.child(playerEntity.getName());
                    playerReference.setValue(playerEntity);
                }
            });
        } catch (Exception e) {
            Log.w("Firebase Error", "Error during saving game");
        }
    }

    void update(List<PlayerEntity> playersToUpdate) {
        // First, update in local db
        DxitDatabase.databaseWriteExecutor.execute(() -> playerDao.update(playersToUpdate));

        try {
            final Map<String, Object> playersToUpdateForFirebase = new HashMap<>();
            for (PlayerEntity playerEntity : playersToUpdate) {
                playersToUpdateForFirebase.put(playerEntity.getName(), playerEntity);
            }
            FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {

                if (task.getResult() != null) {
                    DatabaseReference playersReference = firebaseDatabase
                            .getReference(task.getResult())
                            .child(FirebaseReferencesEnum.PLAYERS.name());
                    playersReference.updateChildren(playersToUpdateForFirebase);
                }
            });
        } catch (Exception e) {
            Log.w("Firebase Error", "Error during saving game");
        }
    }
}
